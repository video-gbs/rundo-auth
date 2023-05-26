package com.runjian.rbac.service.system.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.domain.dto.system.QuerySysApiInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysApiInfoDTO;
import com.runjian.rbac.domain.dto.system.SysApiInfoDTO;
import com.runjian.rbac.domain.entity.ApiInfo;
import com.runjian.rbac.domain.entity.RoleApi;
import com.runjian.rbac.domain.vo.system.SysApiInfoVO;
import com.runjian.rbac.domain.vo.tree.ApiInfoTree;
import com.runjian.rbac.mapper.ApiInfoMapper;
import com.runjian.rbac.service.system.ApiInfoService;
import com.runjian.rbac.service.system.RoleApiService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 接口信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Lazy
    @Autowired
    private RoleApiService roleApiService;

    @Transactional
    @Override
    public void save(SysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        ApiInfo parentApiInfo = apiInfoMapper.selectById(dto.getApiPid());
        apiInfo.setApiPids(parentApiInfo.getApiPids() + "[" + dto.getApiPid() + "]");
        apiInfo.setLevel(parentApiInfo.getLevel() + 1);
        apiInfo.setLeaf(0);
        apiInfoMapper.insert(apiInfo);
        RoleApi roleApi = new RoleApi();
        roleApi.setRoleId(1L);
        roleApi.setApiId(apiInfo.getId());
        roleApiService.save(roleApi);
    }

    @Transactional
    @Override
    public void modifyById(SysApiInfoDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public SysApiInfoVO findById(Long id) {
        ApiInfo apiInfo = apiInfoMapper.selectById(id);
        SysApiInfoVO sysApiInfoVO = new SysApiInfoVO();
        BeanUtils.copyProperties(apiInfo, sysApiInfoVO);
        return sysApiInfoVO;
    }

    @Override
    public Page<SysApiInfoVO> findByPage(Integer pageNum, Integer pageSize) {
        Page<SysApiInfoVO> page = new Page<>(pageNum, pageSize);
        return apiInfoMapper.MySelectPage(page);
    }

    @Transactional
    @Override
    public void modifyByStatus(StatusSysApiInfoDTO dto) {
        ApiInfo apiInfo = apiInfoMapper.selectById(dto.getId());
        apiInfo.setStatus(dto.getStatus());
        apiInfoMapper.updateById(apiInfo);
    }

    @Override
    public List<Tree<Long>> findByTree(QuerySysApiInfoDTO dto) {
        List<ApiInfo> apiInfoList = getByList(dto);
        List<ApiInfoTree> apiInfoTreeList = apiInfoList.stream().map(
                item -> {
                    ApiInfoTree bean = new ApiInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return getTree(apiInfoTreeList);
    }

    @Override
    public List<Tree<Long>> getTreeByAppId(Long appId) {
        LambdaQueryWrapper<ApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiInfo::getAppId, appId);
        List<ApiInfo> apiInfoList = apiInfoMapper.selectList(queryWrapper);
        List<ApiInfoTree> apiInfoTreeList = apiInfoList.stream().map(
                item -> {
                    ApiInfoTree bean = new ApiInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    return bean;
                }
        ).collect(Collectors.toList());
        return getTree(apiInfoTreeList);
    }

    private List<Tree<Long>> getTree(List<ApiInfoTree> apiInfoTreeList){
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        apiInfoTreeList.forEach(e -> {

            TreeNode<Long> treeNode = new TreeNode<>(e.getId(), e.getParentId(), e.getApiName(), e.getApiSort());
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("level", e.getLevel());
            extraMap.put("appId", e.getAppId());
            extraMap.put("url", e.getUrl());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        });
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig() {{
            setIdKey("id");
            setNameKey("apiName");
            setParentIdKey("apiPid");
            setChildrenKey("children");
            setWeightKey("apiSort");
        }};
        return TreeUtil.build(nodeList, 0L, treeNodeConfig, new DefaultNodeParser<>());
    }

    @Override
    public List<ApiInfo> getApiInfoByRoleCode(String roleCode) {
        return apiInfoMapper.selectApiInfoByRoleCode(roleCode);
    }

    @Override
    public List<Long> getApiIdListByRoleId(Long roleId) {
        return apiInfoMapper.findApiIdListByRoleId(roleId);
    }

    @Override
    public List<SysApiInfoVO> findByList(QuerySysApiInfoDTO dto) {
        return getByList(dto).stream().map(
                item -> {
                    SysApiInfoVO vo = new SysApiInfoVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                }
        ).collect(Collectors.toList());
    }


    private List<ApiInfo> getByList(QuerySysApiInfoDTO dto) {
        LambdaQueryWrapper<ApiInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (null != dto.getUrl()) {
            queryWrapper.eq(ApiInfo::getUrl, dto.getUrl());
        }
        if (null != dto.getApiName()) {
            queryWrapper.eq(ApiInfo::getApiName, dto.getApiName());
        }
        if (null != dto.getAppId()) {
            queryWrapper.eq(ApiInfo::getAppId, dto.getAppId()).or().eq(ApiInfo::getAppId, 0L);
        }
        return apiInfoMapper.selectList(queryWrapper);
    }

}
