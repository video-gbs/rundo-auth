package com.runjian.rbac.service.rbac.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonEnum;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.MethodType;
import com.runjian.rbac.dao.FuncMapper;
import com.runjian.rbac.dao.MenuMapper;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.dao.relation.RoleFuncMapper;
import com.runjian.rbac.entity.FuncInfo;
import com.runjian.rbac.entity.MenuInfo;
import com.runjian.rbac.entity.relation.FuncResourceRel;
import com.runjian.rbac.entity.relation.RoleFuncRel;
import com.runjian.rbac.service.auth.CacheService;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.FuncService;
import com.runjian.rbac.vo.dto.CacheFuncDto;
import com.runjian.rbac.vo.response.GetFuncPageRsp;
import com.runjian.rbac.vo.response.GetFuncResourceRsp;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miracle
 * @date 2023/6/6 10:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FuncServiceImpl implements FuncService {

    private final FuncMapper funcMapper;

    private final DataBaseService dataBaseService;

    private final FuncResourceMapper funcResourceMapper;

    private final MenuMapper menuMapper;

    private final RoleFuncMapper roleFuncMapper;

    private final CacheService cacheService;

    @Override
    @PostConstruct
    public void initFuncCache() {
        List<FuncInfo> funcInfoList = funcMapper.selectAll();
        if (funcInfoList.isEmpty()){
            return;
        }
        List<Long> funcIds = funcInfoList.stream().map(FuncInfo::getId).toList();
        Map<Long, List<FuncResourceRel>> funcResourceMap = funcResourceMapper.selectByFuncIdIn(funcIds).stream().collect(Collectors.groupingBy(FuncResourceRel::getFuncId));
        Map<Long, List<RoleFuncRel>> roleFuncMap = roleFuncMapper.selectByFuncIdIn(funcIds).stream().collect(Collectors.groupingBy(RoleFuncRel::getFuncId));
        Map<String, String> funcCache = new HashMap<>(funcInfoList.size());
        for (FuncInfo funcInfo : funcInfoList){
            CacheFuncDto cacheFuncDto = new CacheFuncDto();
            cacheFuncDto.setScope(funcInfo.getScope());
            cacheFuncDto.setFuncName(funcInfo.getFuncName());
            List<RoleFuncRel> roleFuncRels = roleFuncMap.get(funcInfo.getId());
            if (!CollectionUtils.isEmpty(roleFuncRels)) {
                cacheFuncDto.setRoleIds(roleFuncRels.stream().map(RoleFuncRel::getRoleId).toList());
            }
            List<FuncResourceRel> funcResourceRelList = funcResourceMap.get(funcInfo.getId());
            if (!CollectionUtils.isEmpty(funcResourceRelList)){
                cacheFuncDto.setFuncResourceDataList(funcResourceRelList.stream().map(CacheFuncDto.FuncResourceData::new).toList());
            }
            funcCache.put(MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath(), JSONObject.toJSONString(cacheFuncDto));
        }
        cacheService.setAllFuncCache(funcCache);
    }

    @Override
    public PageInfo<GetFuncPageRsp> getFuncPage(int page, int num, Long menuId, String serviceName, String funcName, Boolean isInclude) {
        Set<Long> menuIds = new HashSet<>();
        menuIds.add(menuId);
        if (isInclude){
            MenuInfo menuInfo;
            if (menuId.equals(0L)){
                menuIds.addAll(menuMapper.selectAllId());
            }else {
                menuInfo = dataBaseService.getMenuInfo(menuId);
                menuIds.addAll(menuMapper.selectIdByLevelLike(menuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + menuInfo.getId()));
            }
        }
        PageHelper.startPage(page, num);
        return new PageInfo<>(funcMapper.selectAllByMenuIdAndServiceNameLikeAndFuncNameLike(menuIds, serviceName, funcName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFunc(Long menuId, String serviceName, String funcName, String scope, String path, Integer method) {
        Optional<FuncInfo> funcInfoOp = funcMapper.selectByPath(path);
        if (funcInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("重复定义的资源路径 %s", path));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        FuncInfo funcInfo = new FuncInfo();
        funcInfo.setMenuId(menuId);
        funcInfo.setServiceName(serviceName);
        funcInfo.setFuncName(funcName);
        funcInfo.setScope(scope);
        funcInfo.setPath(path);
        funcInfo.setMethod(method);
        funcInfo.setDisabled(CommonEnum.DISABLE.getCode());
        funcInfo.setUpdateTime(nowTime);
        funcInfo.setCreateTime(nowTime);
        funcMapper.save(funcInfo);
        CacheFuncDto cacheFuncDto = new CacheFuncDto();
        cacheFuncDto.setScope(scope);
        cacheFuncDto.setFuncName(funcName);
        cacheService.setFuncCache(MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath(), cacheFuncDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDisabled(Long id, Integer disabled) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(id);
        funcInfo.setDisabled(disabled);
        funcInfo.setUpdateTime(LocalDateTime.now());
        String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
        if (CommonEnum.getBoolean(disabled)){
            cacheService.removeFuncCache(key);
        }else {
            addFuncCache(funcInfo);
        }
        funcMapper.updateDisabled(funcInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFunc(Long id, Long menuId, String serviceName, String funcName, String scope, String path, Integer method) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(id);
        if (!funcInfo.getPath().equals(path) || !funcInfo.getMethod().equals(method)){
            Optional<FuncInfo> funcInfoOp = funcMapper.selectByPathAndMethod(method, path);
            if (funcInfoOp.isPresent()){
                throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("重复定义的资源 %s:%s", MethodType.getByCode(method).getMsg(), path));
            }
        }
        cacheService.removeFuncCache(MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath());
        if (!funcInfo.getMenuId().equals(menuId)){
            dataBaseService.getMenuInfo(menuId);
            funcInfo.setMenuId(menuId);
        }
        funcInfo.setServiceName(serviceName);
        funcInfo.setFuncName(funcName);
        funcInfo.setScope(scope);
        funcInfo.setPath(path);
        funcInfo.setMethod(method);
        funcInfo.setUpdateTime(LocalDateTime.now());
        funcMapper.update(funcInfo);
        addFuncCache(funcInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunc(Long id) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(id);
        funcResourceMapper.deleteAllByFuncId(id);
        roleFuncMapper.deleteAllByFuncId(id);
        funcMapper.deleteById(id);
        cacheService.removeFuncCache(MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath());
    }

    @Override
    public List<GetFuncResourceRsp> getFuncResource(Long funcId) {
        return funcResourceMapper.selectByFuncId(funcId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateResource(Long funcId, String resourceKey, String validateParam, String multiGroup) {
        LocalDateTime nowTime = LocalDateTime.now();
        FuncInfo funcInfo = dataBaseService.getFuncInfo(funcId);
        FuncResourceRel funcResourceRel = new FuncResourceRel();
        funcResourceRel.setFuncId(funcId);
        funcResourceRel.setResourceKey(resourceKey);
        funcResourceRel.setValidateParam(validateParam);
        funcResourceRel.setDisabled(CommonEnum.DISABLE.getCode());
        funcResourceRel.setUpdateTime(nowTime);
        funcResourceRel.setCreateTime(nowTime);
        funcResourceRel.setMultiGroup(multiGroup);
        funcResourceMapper.save(funcResourceRel);
        String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
        CacheFuncDto funcCache = cacheService.getFuncCache(key);
        funcCache.getFuncResourceDataList().add(new CacheFuncDto.FuncResourceData(funcResourceRel));
        cacheService.setFuncCache(key, funcCache);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFuncResourceDisabled(Long funcResourceId, Integer disabled) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，请重新刷新", funcResourceId));
        }

        FuncResourceRel funcResourceRel = funcResourceRelOp.get();
        funcResourceRel.setDisabled(disabled);
        funcResourceRel.setUpdateTime(LocalDateTime.now());
        funcResourceMapper.updateDisabled(funcResourceRel);

        FuncInfo funcInfo = dataBaseService.getFuncInfo(funcResourceRel.getFuncId());
        String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
        CacheFuncDto funcCache = cacheService.getFuncCache(key);
        CacheFuncDto.FuncResourceData funcResourceData = new CacheFuncDto.FuncResourceData(funcResourceRel);
        if (CommonEnum.getBoolean(disabled)){
            funcCache.getFuncResourceDataList().remove(funcResourceData);
        }else {
            funcCache.getFuncResourceDataList().add(funcResourceData);
        }
        cacheService.setFuncCache(key, funcCache);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFuncResource(Long funcResourceId, String resourceKey, String validateParam, String multiGroup) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，请重新刷新", funcResourceId));
        }
        FuncResourceRel funcResourceRel = funcResourceRelOp.get();
        Boolean isDisabled = CommonEnum.getBoolean(funcResourceRel.getDisabled());
        if (!isDisabled){
            resetCacheFuncResource(funcResourceRel, null);
        }
        resetCacheFuncResource(funcResourceRel, null);
        funcResourceRel.setResourceKey(resourceKey);
        funcResourceRel.setValidateParam(validateParam);
        funcResourceRel.setMultiGroup(multiGroup);
        funcResourceMapper.update(funcResourceRel);
        if (!isDisabled){
            resetCacheFuncResource(funcResourceRel, funcResourceRel);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFuncResource(Long funcResourceId) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，或已被删除，请重新刷新", funcResourceId));
        }
        funcResourceMapper.deleteById(funcResourceId);
        FuncResourceRel funcResourceRel = funcResourceRelOp.get();
        resetCacheFuncResource(funcResourceRel, null);
    }

    /**
     * 添加功能缓存
     * @param funcInfo 功能信息
     */
    private void addFuncCache(FuncInfo funcInfo) {
        CacheFuncDto cacheFuncDto = new CacheFuncDto();
        cacheFuncDto.setScope(funcInfo.getScope());
        cacheFuncDto.setFuncName(funcInfo.getFuncName());
        cacheFuncDto.setRoleIds(roleFuncMapper.selectRoleIdsByFuncId(funcInfo.getId()));
        cacheFuncDto.setFuncResourceDataList(funcResourceMapper.selectFuncResourceDataByFuncId(funcInfo.getId()));
        cacheService.setFuncCache(MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath(), cacheFuncDto);
    }

    /**
     * 移除功能资源缓存
     * @param funcResourceRel 功能资源关系数据
     */
    private void resetCacheFuncResource(FuncResourceRel funcResourceRel, FuncResourceRel newFuncResourceRel) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(funcResourceRel.getFuncId());
        String key = MethodType.getByCode(funcInfo.getMethod()) + MarkConstant.MARK_SPLIT_SEMICOLON + funcInfo.getPath();
        CacheFuncDto funcCache = cacheService.getFuncCache(key);
        funcCache.getFuncResourceDataList().remove(new CacheFuncDto.FuncResourceData(funcResourceRel));
        if (Objects.nonNull(newFuncResourceRel)){
            funcCache.getFuncResourceDataList().add(new CacheFuncDto.FuncResourceData(newFuncResourceRel));
        }
        cacheService.setFuncCache(key, funcCache);
    }
}
