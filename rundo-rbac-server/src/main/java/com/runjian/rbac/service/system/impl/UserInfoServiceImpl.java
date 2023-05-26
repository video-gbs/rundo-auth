package com.runjian.rbac.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.constant.StatusConstant;
import com.runjian.rbac.domain.dto.page.PageRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.page.PageSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.QueryRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.QuerySysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.SysUserInfoDTO;
import com.runjian.rbac.domain.entity.RoleUser;
import com.runjian.rbac.domain.entity.UserInfo;
import com.runjian.rbac.domain.vo.system.EditSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.ListSysUserInfoVO;
import com.runjian.rbac.domain.vo.system.OrgInfoVO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.rbac.mapper.OrgInfoMapper;
import com.runjian.rbac.mapper.UserInfoMapper;
import com.runjian.rbac.service.system.*;
import com.runjian.rbac.util.RoleIdUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private OrgInfoMapper orgInfoMapper;

    @Autowired
    private OrgInfoService orgInfoService;

    @Lazy
    @Autowired
    private RoleInfoService roleInfoService;

    @Lazy
    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private VideoAreaService videoAreaService;

    @Lazy
    @Autowired
    private RoleOrgService roleOrgService;

    @Autowired
    private RoleIdUtil roleIdUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public void save(SysUserInfoDTO dto) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserAccount, dto.getUserAccount());
        long count = userInfoMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(BusinessErrorEnums.VALID_OBJECT_IS_EXIST, "账号已存在");
        }
        // 处理基本信息
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(dto, userInfo);
        // 处理密码
        String password = passwordEncoder.encode(dto.getPassword());
        userInfo.setPassword(password);
        userInfo.setStatus(StatusConstant.ENABLE);
        // sysUserInfo.setTenantId();
        userInfoMapper.insert(userInfo);
        // 处理角色信息
        List<Long> roleIds = dto.getRoleIds();
        if (roleIds.size() > 0) {
            saveRoleUser(roleIds, userInfo.getId());
        }
    }

    @Override
    public EditSysUserInfoVO findById(Long id) {
        EditSysUserInfoVO vo = new EditSysUserInfoVO();
        UserInfo userInfo = userInfoMapper.selectById(id);
        BeanUtils.copyProperties(userInfo, vo);
        OrgInfoVO orgInfoVO = orgInfoService.getOrgInfoByUserId(id);
        List<Long> roleIds = roleInfoService.getRoleByUserId(id);
        vo.setPassword(null);
        vo.setRePassword(null);
        vo.setOrgId(orgInfoVO.getOrgId());
        vo.setOrgName(orgInfoVO.getOrgName());
        vo.setRoleIds(roleIds);
        return vo;
    }

    @Transactional
    @Override
    public void modifyById(SysUserInfoDTO dto) {
        // 根据id查取原始信息
        UserInfo userInfo = userInfoMapper.selectById(dto.getId());
        // 处理信息
        if (!"".equals(dto.getPassword())) {
            String password = passwordEncoder.encode(dto.getPassword());
            dto.setPassword(password);
        } else {
            dto.setPassword(userInfo.getPassword());
        }
        BeanUtils.copyProperties(dto, userInfo);
        userInfoMapper.updateById(userInfo);

        // 根据id查取角色信息
        List<Long> oldRoleIds = roleInfoService.getRoleByUserId(dto.getId());
        log.debug("原始角色信息{}", JSONUtil.toJsonStr(oldRoleIds));
        // 获取本次传输得角色ID
        List<Long> newRoleIds = dto.getRoleIds();
        log.debug("本次传输得角色信息{}", JSONUtil.toJsonStr(newRoleIds));
        // 取出要回收的角色权限
        List<Long> commonRoleIds = newRoleIds.stream().filter(oldRoleIds::contains).collect(Collectors.toList());
        log.debug("新旧相同的角色信息{}", JSONUtil.toJsonStr(commonRoleIds));
        // 取出要回收的角色信息
        List<Long> removeRoleIds = oldRoleIds.stream().filter(item -> !commonRoleIds.contains(item)).collect(Collectors.toList());
        log.debug("要回收的角色信息{}", JSONUtil.toJsonStr(removeRoleIds));
        removeRoleUser(removeRoleIds, userInfo.getId());
        // 取出要新的授权的角色权限
        List<Long> roleIds = newRoleIds.stream().filter(item -> !commonRoleIds.contains(item)).collect(Collectors.toList());
        log.debug("新授权的角色信息{}", JSONUtil.toJsonStr(roleIds));
        saveRoleUser(roleIds, userInfo.getId());
    }

    @Override
    public void modifyByStatus(StatusSysUserInfoDTO dto) {
        UserInfo userInfo = userInfoMapper.selectById(dto.getId());
        userInfo.setStatus(dto.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Page<ListSysUserInfoVO> findByPage(QuerySysUserInfoDTO dto) {
        // 1、查询该用户已有的部门权限
        // 取出当前登录的用户的所有角色
        List<Long> roleIds = roleIdUtil.getRoleIdList();
        if(CollectionUtil.isEmpty(roleIds)){
            return null;
        }
        List<Long> orgIds = roleIdUtil.getRoleOrgIdList(roleIds);
        if(CollectionUtil.isEmpty(orgIds)){
            // 已授权部门为空，不论是否勾选下级都直接返回
            return null;
        }
        List<String> orgIdStr = orgIds.stream().map(String::valueOf).collect(Collectors.toList());
        // 2、判断选中的节点是否在已授权中
        boolean flag = CollectionUtil.contains(orgIdStr, String.valueOf(dto.getOrgId()));
        boolean flag2 = dto.getContain() != 1;
        if (!flag && flag2) {
            // 不在授权内，没勾选下级
            return null;
        }
        if (flag && flag2) {
            // 在授权内，没勾选下级
            return getResult(dto, Collections.singletonList(dto.getOrgId()));
        }
        // 勾选下级，
        List<Long> children = orgInfoMapper.mySelectOrg(dto.getOrgId());
        List<String> childrenStr = children.stream().map(String::valueOf).collect(Collectors.toList());
        childrenStr.removeIf(elem -> !orgIdStr.contains(elem));
        List<Long> childrenLong = childrenStr.stream().map(Long::valueOf).collect(Collectors.toList());
        return getResult(dto, childrenLong);
    }

    private Page<ListSysUserInfoVO> getResult(QuerySysUserInfoDTO dto, List<Long> orgIds) {
        PageSysUserInfoDTO page = new PageSysUserInfoDTO();
        if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return userInfoMapper.MySelectPageByContain(page, orgIds);
    }

    @Transactional
    @Override
    public void erasureById(Long id) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getUserId, id);
        roleUserService.remove(queryWrapper);
        userInfoMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void erasureBatch(List<Long> ids) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getUserId, ids);
        roleUserService.remove(queryWrapper);
        userInfoMapper.deleteBatchIds(ids);
    }

    @Override
    public RelationSysUserInfoVO findRelationById(Long id) {
        RelationSysUserInfoVO vo = userInfoMapper.selectRelationSysUserInfoById(id);
        List<String> areaNameList = videoAreaService.getAreaNameByUserId(id);
        String areaName = String.join(",", areaNameList);
        vo.setAreaName(areaName);
        return vo;
    }

    @Override
    public Page<RelationSysUserInfoVO> findRelationList(QueryRelationSysUserInfoDTO dto) {
        PageRelationSysUserInfoDTO page = new PageRelationSysUserInfoDTO();
        if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (null != dto.getCurrent() && dto.getCurrent() > 0) {
            page.setCurrent(dto.getCurrent());
        } else {
            page.setCurrent(1);
        }
        if (null != dto.getPageSize() && dto.getPageSize() > 0) {
            page.setSize(dto.getPageSize());
        } else {
            page.setSize(20);
        }
        return userInfoMapper.relationSysUserInfoPage(page);
    }

    @Override
    public List<Long> getUserIdListByRoleId(Long roleId) {
        return userInfoMapper.selectUserIdListByRoleId(roleId);
    }

    private void saveRoleUser(List<Long> roleIds, Long userId) {
        for (Long roleId : roleIds) {
            roleInfoService.saveRoleUser(roleId, userId);
        }
    }

    private void removeRoleUser(List<Long> roleIds, Long userId) {
        for (Long roleId : roleIds) {
            roleInfoService.removeRoleUser(roleId, userId);
        }
    }
}
