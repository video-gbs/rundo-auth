package com.runjian.rbac.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.constant.DefaultConstant;
import com.runjian.rbac.constant.StatusConstant;
import com.runjian.rbac.domain.dto.system.MoveSysOrgDTO;
import com.runjian.rbac.domain.dto.system.SysOrgDTO;
import com.runjian.rbac.domain.entity.OrgInfo;
import com.runjian.rbac.domain.entity.RoleOrg;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.vo.system.OrgInfoVO;
import com.runjian.rbac.domain.vo.system.SysOrgVO;
import com.runjian.rbac.domain.vo.tree.SysOrgTree;
import com.runjian.rbac.mapper.OrgInfoMapper;
import com.runjian.rbac.mapper.UserInfoMapper;
import com.runjian.rbac.service.system.OrgInfoService;
import com.runjian.rbac.service.system.RoleOrgService;
import com.runjian.rbac.util.RoleIdUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.config.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织机构表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class OrgInfoServiceImpl extends ServiceImpl<OrgInfoMapper, OrgInfo> implements OrgInfoService {

    @Autowired
    private OrgInfoMapper orgInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Lazy
    @Autowired
    private RoleOrgService roleOrgService;

    @Autowired
    private RoleIdUtil roleIdUtil;

    @Transactional
    @Override
    public SysOrgVO save(SysOrgDTO dto) {
        OrgInfo parentInfo = orgInfoMapper.selectById(dto.getOrgPid());
        if (null == parentInfo) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND, "上级部门不存在");
        }
        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgPid(parentInfo.getId());
        orgInfo.setOrgName(dto.getOrgName());
        if (null != dto.getOrgSort()) {
            orgInfo.setOrgSort(dto.getOrgSort());
        }
        orgInfo.setOrgSort(DefaultConstant.SORT);
        orgInfo.setAdders(dto.getAdders());
        orgInfo.setOrgLeader(dto.getOrgLeader());
        orgInfo.setEmail(dto.getEmail());
        orgInfo.setPhone(dto.getPhone());
        orgInfo.setLevel(parentInfo.getLevel() + 1);
        orgInfo.setDescription(dto.getDescription());
        orgInfo.setStatus(StatusConstant.ENABLE);
        log.info("添加部门入库数据信息{}", JSONUtil.toJsonStr(orgInfo));
        orgInfoMapper.insert(orgInfo);
        RoleOrg roleOrg = new RoleOrg();
        roleOrg.setOrgId(orgInfo.getId());
        roleOrg.setRoleId(1L);
        roleOrgService.save(roleOrg);
        // 回显数据给前端
        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(orgInfo, sysOrgVO);
        return sysOrgVO;
    }

    @Transactional
    @Override
    public void modifyById(SysOrgDTO dto) {
        OrgInfo orgInfo = new OrgInfo();
        BeanUtils.copyProperties(dto, orgInfo);
        orgInfoMapper.updateById(orgInfo);
    }

    @Override
    public SysOrgVO findById(Long id) {
        OrgInfo orgInfo = orgInfoMapper.selectById(id);
        SysOrgVO sysOrgVO = new SysOrgVO();
        BeanUtils.copyProperties(orgInfo, sysOrgVO);
        return sysOrgVO;
    }

    @Transactional
    @Override
    public CommonResponse erasureById(Long id) {
        // 1.判断是否为根节点
        if (id == 1L) {
            return CommonResponse.failure(BusinessErrorEnums.DEFAULT_MEDIA_DELETE_ERROR);
        }
        // 2.确认当前需要删除的部门有无下级部门
        OrgInfo orgInfo = orgInfoMapper.selectById(id);
        List<SysOrgVO> orgInfoChildren = orgInfoMapper.mySelectListById(id);
        int size = orgInfoChildren.size();
        for (int i = size - 1; i >= 0; i--) {
            SysOrgVO org = orgInfoChildren.get(i);
            if (org.getId().equals(orgInfo.getId())) {
                // 2-1.剔除自己之后确认是否还存在子节点
                orgInfoChildren.remove(org);
            }
        }
        // 2-2.剔除自己之后确认是否还存在子节点
        if (CollUtil.isNotEmpty(orgInfoChildren)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_ORG_OPERATION2);
        }
        // 3.判断该部门是否有员工
        LambdaQueryWrapper<UserInfo> userInfoQueryWrapper = new LambdaQueryWrapper<>();
        userInfoQueryWrapper.eq(UserInfo::getOrgId, id);
        List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQueryWrapper);
        if (CollUtil.isNotEmpty(userInfoList)) {
            return CommonResponse.failure(BusinessErrorEnums.VALID_ILLEGAL_ORG_OPERATION);
        }
        // 4.删除目标节点
        LambdaQueryWrapper<RoleOrg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleOrg::getOrgId,id);
        roleOrgService.remove(wrapper);
        return CommonResponse.success(orgInfoMapper.deleteById(id));
    }

    @Override
    public List<SysOrgVO> findByList() {
        List<OrgInfo> orgInfoList = orgInfoMapper.selectList(null);
        return orgInfoList.stream().map(
                item -> {
                    SysOrgVO sysOrgVO = new SysOrgVO();
                    BeanUtils.copyProperties(item, sysOrgVO);
                    return sysOrgVO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void moveSysOrg(MoveSysOrgDTO dto) {
        // 禁止本级移动到本级
        if (dto.getId().equals(dto.getOrgPid())) {
            throw new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "禁止本级移动到本级");
        }
        // 目标位置的信息
        OrgInfo targetOrg = orgInfoMapper.selectById(dto.getOrgPid());
        // 被移动项信息
        OrgInfo selectOrg = orgInfoMapper.selectById(dto.getId());
        // 判断目标是否存在
        Optional.ofNullable(selectOrg).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "移动的节点不存在或已删除"));
        Optional.ofNullable(targetOrg).orElseThrow(() -> new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "目标位置不存在或已删除"));
        // 1. 确认是否为同一层级节点移动
        if (selectOrg.getOrgPid().equals(targetOrg.getOrgPid())) {
            log.info("同一层级节点移动");
            // 交换排序顺序
            selectOrg.setOrgSort(targetOrg.getOrgSort());
            orgInfoMapper.updateById(selectOrg);
            targetOrg.setOrgSort(selectOrg.getOrgSort());
            orgInfoMapper.updateById(targetOrg);
            return;
        }
        // 2. 判断是否为祖父级向子孙级别移动
        List<SysOrgVO> childrenList = orgInfoMapper.mySelectListById(dto.getId());
        List<Long> ids = childrenList.stream().map(SysOrgVO::getId).collect(Collectors.toList());
        if (ids.contains(dto.getOrgPid())) {
            log.info("父级向子级移动");
            throw new BusinessException(BusinessErrorEnums.VALID_METHOD_NOT_SUPPORTED, "禁止向子级移动");
        }
        // 3.更新当前节点信息
        selectOrg.setOrgPid(targetOrg.getId());
        selectOrg.setLevel(targetOrg.getLevel() + 1);
        orgInfoMapper.updateById(selectOrg);
        // 4.更新子节点信息
        updateChildren(selectOrg.getId(), selectOrg.getLevel());
    }

    @Override
    public List<OrgInfo> getOrgInfoByRoleCode(String roleCode) {
        return orgInfoMapper.selectOrgInfoByRoleCode(roleCode);
    }

    @Override
    public List<Long> getOrgIdListByRoleId(Long roleId) {
        return orgInfoMapper.findOrgIdListByRoleId(roleId);
    }

    @Override
    public OrgInfoVO getOrgInfoByUserId(Long id) {
        return orgInfoMapper.selectOrgInfoByUserId(id);
    }

    @Override
    public List<Tree<Long>> findByTree() {
        // 获取已有角色
        List<Long> roleIds = roleIdUtil.getRoleIdList();
        if(CollectionUtil.isEmpty(roleIds)){
            return null;
        }
        List<Long> roleOrgIds = roleIdUtil.getRoleOrgIdList(roleIds);
        if(CollectionUtil.isEmpty(roleOrgIds)){
            return null;
        }
        // 递归获取部门树
        List<OrgInfo> orgInfoList = orgInfoMapper.selectOrgList(roleOrgIds);
        orgInfoList.stream().distinct();
        List<SysOrgTree> sysOrgTreeList = orgInfoList.stream().map(item -> {
            SysOrgTree bean = new SysOrgTree();
            BeanUtils.copyProperties(item, bean);
            return bean;
        }).collect(Collectors.toList());
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        sysOrgTreeList.forEach(e -> {
            TreeNode<Long> treeNode = new TreeNode<>(e.getId(), e.getParentId(), e.getOrgName(), e.getOrgSort());
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("level", e.getLevel());
            extraMap.put("orgLeader", e.getOrgLeader());
            extraMap.put("phone", e.getPhone());
            extraMap.put("email", e.getEmail());
            extraMap.put("adders", e.getAdders());
            extraMap.put("description", e.getDescription());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        });
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig() {{
            setIdKey("id");
            setNameKey("orgName");
            setParentIdKey("orgPid");
            setChildrenKey("children");
            setWeightKey("orgSort");
        }};
        return TreeUtil.build(nodeList, 0L, treeNodeConfig, new DefaultNodeParser<>());
    }

    @Override
    public IPage<OrgInfo> findByPage(Integer pageNum, Integer pageSize) {
        Page<OrgInfo> page = Page.of(pageNum, pageSize);
        return orgInfoMapper.selectPage(page, null);
    }

    private void updateChildren(Long id, Integer level) {
        List<OrgInfo> infoList = getChildren(id);
        infoList.stream().peek(obj -> obj.setLevel(level + 1)).collect(Collectors.toList());
        updateBatchById(infoList);
        for (OrgInfo org : infoList) {
            updateChildren(org.getId(), org.getLevel());
        }
    }

    private List<OrgInfo> getChildren(Long id) {
        LambdaQueryWrapper<OrgInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrgInfo::getOrgPid, id);
        queryWrapper.orderBy(true, true, OrgInfo::getOrgSort, OrgInfo::getUpdatedTime);
        return orgInfoMapper.selectList(queryWrapper);
    }
}
