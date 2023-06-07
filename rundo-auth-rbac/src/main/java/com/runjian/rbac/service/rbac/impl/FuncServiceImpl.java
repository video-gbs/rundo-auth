package com.runjian.rbac.service.rbac.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.FuncMapper;
import com.runjian.rbac.dao.MenuMapper;
import com.runjian.rbac.dao.relation.FuncResourceMapper;
import com.runjian.rbac.entity.FuncInfo;
import com.runjian.rbac.entity.MenuInfo;
import com.runjian.rbac.entity.relation.FuncResourceRel;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.FuncService;
import com.runjian.rbac.vo.response.GetFuncPageRsp;
import com.runjian.rbac.vo.response.GetFuncResourceRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Override
    public PageInfo<GetFuncPageRsp> getFuncPage(int page, int num, Long menuId, String serviceName, String funcName, Boolean isInclude) {
        Set<Long> menuIds = new HashSet<>();
        menuIds.add(menuId);
        if (isInclude){
            MenuInfo menuInfo = dataBaseService.getMenuInfo(menuId);
            menuIds.addAll(menuMapper.selectIdByLevelLike(menuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + menuInfo.getId()));
        }
        PageHelper.startPage(page, num);
        return new PageInfo<>(funcMapper.selectAllByMenuIdAndServiceNameLikeAndFuncNameLike(menuIds, serviceName, funcName));
    }

    @Override
    public void addFunc(Long menuId, String serviceName, String funcName, String path, Integer method, Integer disabled) {
        Optional<FuncInfo> funcInfoOp = funcMapper.selectByPath(path);
        if (funcInfoOp.isPresent()){
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("重复定义的资源路径 %s", path));
        }
        LocalDateTime nowTime = LocalDateTime.now();
        FuncInfo funcInfo = new FuncInfo();
        funcInfo.setMenuId(menuId);
        funcInfo.setServiceName(serviceName);
        funcInfo.setFuncName(funcName);
        funcInfo.setPath(path);
        funcInfo.setMethod(method);
        funcInfo.setDisabled(disabled);
        funcInfo.setUpdateTime(nowTime);
        funcInfo.setCreateTime(nowTime);
        funcMapper.save(funcInfo);
    }

    @Override
    public void updateDisabled(Long id, Integer disabled) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(id);
        funcInfo.setDisabled(disabled);
        funcInfo.setUpdateTime(LocalDateTime.now());
        funcMapper.updateDisabled(funcInfo);
    }

    @Override
    public void updateFunc(Long id, Long menuId, String serviceName, String funcName, String path, Integer method, Integer disabled) {
        FuncInfo funcInfo = dataBaseService.getFuncInfo(id);
        if (!funcInfo.getPath().equals(path)){
            Optional<FuncInfo> funcInfoOp = funcMapper.selectByPath(path);
            if (funcInfoOp.isPresent()){
                throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, String.format("重复定义的资源路径 %s", path));
            }
        }
        if (!funcInfo.getMenuId().equals(menuId)){
            dataBaseService.getMenuInfo(menuId);
            funcInfo.setMenuId(menuId);
        }
        funcInfo.setServiceName(serviceName);
        funcInfo.setFuncName(funcName);
        funcInfo.setPath(path);
        funcInfo.setMethod(method);
        funcInfo.setDisabled(disabled);
        funcInfo.setUpdateTime(LocalDateTime.now());
        funcMapper.update(funcInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunc(Long id) {
        dataBaseService.getFuncInfo(id);
        funcResourceMapper.deleteAllByFuncId(id);
        funcMapper.deleteById(id);
    }

    @Override
    public List<GetFuncResourceRsp> getFuncResource(Long funcId) {
        return funcResourceMapper.selectByFuncId(funcId);
    }

    @Override
    public void associationResource(Long funcId, String resourceKey, String validateParam, Integer disabled) {
        LocalDateTime nowTime = LocalDateTime.now();
        FuncResourceRel funcResourceRel = new FuncResourceRel();
        funcResourceRel.setFuncId(funcId);
        funcResourceRel.setResourceKey(resourceKey);
        funcResourceRel.setValidateParam(validateParam);
        funcResourceRel.setDisabled(disabled);
        funcResourceRel.setUpdateTime(nowTime);
        funcResourceRel.setCreateTime(nowTime);
        funcResourceMapper.save(funcResourceRel);
    }

    @Override
    public void updateFuncResourceDisabled(Long funcResourceId, Integer disabled) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，请重新刷新", funcResourceId));
        }
        FuncResourceRel funcResourceRel = funcResourceRelOp.get();
        funcResourceRel.setDisabled(disabled);
        funcResourceRel.setUpdateTime(LocalDateTime.now());
        funcResourceMapper.updateDisabled(funcResourceRel);
    }

    @Override
    public void updateFuncResource(Long funcResourceId, String resourceKey, String validateParam, Integer disabled) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，请重新刷新", funcResourceId));
        }
        FuncResourceRel funcResourceRel = funcResourceRelOp.get();
        funcResourceRel.setResourceKey(resourceKey);
        funcResourceRel.setValidateParam(validateParam);
        funcResourceRel.setDisabled(disabled);
    }

    @Override
    public void deleteFuncResource(Long funcResourceId) {
        Optional<FuncResourceRel> funcResourceRelOp = funcResourceMapper.selectById(funcResourceId);
        if (funcResourceRelOp.isEmpty()){
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, String.format("关系资源 %s 不存在，或已被删除，请重新刷新", funcResourceId));
        }
        funcResourceMapper.deleteById(funcResourceId);
    }
}
