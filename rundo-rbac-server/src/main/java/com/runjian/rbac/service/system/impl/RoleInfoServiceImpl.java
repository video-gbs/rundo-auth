package com.runjian.rbac.service.system.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.constant.AppTypeConstant;
import com.runjian.rbac.constant.StatusConstant;
import com.runjian.rbac.domain.dto.page.PageEditUserSysRoleInfoDTO;
import com.runjian.rbac.domain.dto.page.PageRoleRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.page.PageSysRoleInfoDto;
import com.runjian.rbac.domain.dto.system.*;
import com.runjian.rbac.domain.entity.*;
import com.runjian.rbac.domain.vo.system.*;
import com.runjian.rbac.domain.vo.tree.AppMenuApiTree;
import com.runjian.rbac.mapper.RoleInfoMapper;
import com.runjian.rbac.service.system.*;
import com.runjian.rbac.util.CUtil;
import com.runjian.rbac.util.RundoIdUtil;
import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Stream;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo> implements RoleInfoService {

    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private MenuInfoService menuInfoService;

    @Autowired
    private ApiInfoService apiInfoService;

    @Autowired
    private OrgInfoService orgInfoService;

    @Autowired
    private VideoAreaService videoAreaService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RundoIdUtil idUtil;
    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private RoleAppService roleAppService;

    @Lazy
    @Autowired
    private RoleMenuService roleMenuService;

    @Lazy
    @Autowired
    private RoleOrgService roleOrgService;

    @Lazy
    @Autowired
    private RoleAreaService roleAreaService;

    @Lazy
    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private CUtil cUtil;

    @Transactional
    @Override
    public void save(SysRoleInfoDTO dto) {
        RoleInfo role = new RoleInfo();
        Long roleId = idUtil.nextId();
        role.setId(roleId);
        role.setRoleName(dto.getRoleName());
        // 角色新建后默认是启用状态： 0启用，1禁用
        role.setStatus(StatusConstant.ENABLE);
        // 角色是一个特殊的权限，ROLE_前缀 用来满足Spring Security规范
        role.setRoleCode("ROLE_" + roleId.toString());
        role.setRoleDesc(dto.getRoleDesc());
        roleInfoMapper.insert(role);
        // 处理数据
        List<Long> idList = Stream.concat(Stream.concat(dto.getAppIds().stream(), dto.getConfigIds().stream()
                ), dto.getDevopsIds().stream()).filter(id -> !"".equals(id))
                .distinct()
                .map(item -> Long.parseLong(StrUtil.removePreAndLowerFirst(item, 2)))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<MenuInfo> menuInfoList = menuInfoService.listByIds(idList);
            List<Long> appIdList = menuInfoList.stream().map(MenuInfo::getAppId).collect(Collectors.toList());
            appIdList = appIdList.stream().distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(appIdList)) {
                List<RoleApp> roleAppList = new ArrayList<>();
                for (Long appId : appIdList) {
                    RoleApp roleApp = new RoleApp();
                    roleApp.setAppId(appId);
                    roleApp.setRoleId(roleId);
                    roleAppList.add(roleApp);
                }
                roleAppService.saveBatch(roleAppList);
            }
            List<Long> menuIdList = menuInfoList.stream().map(MenuInfo::getId).collect(Collectors.toList());
            menuIdList = menuIdList.stream().distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(menuIdList)) {
                List<RoleMenu> roleMenuList = new ArrayList<>();
                for (Long menuId : menuIdList) {
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setMenuId(menuId);
                    roleMenu.setRoleId(roleId);
                    roleMenuList.add(roleMenu);
                }
                roleMenuService.saveBatch(roleMenuList);
            }
        }
        List<Long> orgIds = dto.getOrgIds();
        if (CollUtil.isNotEmpty(orgIds)) {
            List<RoleOrg> roleOrgList = new ArrayList<>();
            for (Long orgId : orgIds) {
                RoleOrg roleOrg = new RoleOrg();
                roleOrg.setOrgId(orgId);
                roleOrg.setRoleId(roleId);
                roleOrgList.add(roleOrg);
            }
            roleOrgService.saveBatch(roleOrgList);
        }
        List<Long> areaIds = dto.getAreaIds();
        if (CollUtil.isNotEmpty(areaIds)) {
            List<RoleArea> roleAreas = new ArrayList<>();
            for (Long areaId : areaIds) {
                RoleArea roleArea = new RoleArea();
                roleArea.setAreaId(areaId);
                roleArea.setRoleId(roleId);
                roleAreas.add(roleArea);
            }
            roleAreaService.saveBatch(roleAreas);
        }
    }

    @Transactional
    @Override
    public void modifyById(SysRoleInfoDTO dto) {
        // 1 查取原始角色基础信息
        RoleInfo roleInfo = roleInfoMapper.selectById(dto.getId());
        roleInfo.setRoleName(dto.getRoleName());
        roleInfo.setRoleDesc(dto.getRoleDesc());
        roleInfoMapper.updateById(roleInfo);

        List<String> idStrList = dto.getAppIds();
        idStrList.addAll(dto.getConfigIds());
        idStrList.addAll(dto.getDevopsIds());
        // 去空 去重 去除开头
        idStrList = idStrList.stream().filter(id -> !"".equals(id)).collect(Collectors.toList());
        idStrList = cUtil.removeRepString(idStrList);
        List<Long> idList = idStrList.stream().map(
                item -> Long.parseLong(StrUtil.removePreAndLowerFirst(item, 2))
        ).collect(Collectors.toList());
        // 通过处理后的id列表，查找本次勾选的菜单相关联的应用id
        if (CollectionUtil.isNotEmpty(idList)) {
            List<MenuInfo> menuInfoList = menuInfoService.listByIds(idList);
            // 本次新的APPID
            List<Long> newAppIdList = menuInfoList.stream().map(MenuInfo::getAppId).collect(Collectors.toList());
            newAppIdList = cUtil.removeRepLong(newAppIdList);
            List<Long> oldAppIdList = appInfoService.getAppIdListByRoleId(dto.getId());
            log.info("原始的应用{}", JSONUtil.toJsonStr(oldAppIdList));
            log.info("新提交的应用{}", JSONUtil.toJsonStr(newAppIdList));
            List<Long> notInNewAppIdList = CollectionUtil.subtractToList(oldAppIdList, newAppIdList);
            log.info("原始中有，新提交没有的应用{}", JSONUtil.toJsonStr(notInNewAppIdList));
            log.info("回收的应用{}", JSONUtil.toJsonStr(notInNewAppIdList));
            if (CollectionUtil.isNotEmpty(notInNewAppIdList)) {
                LambdaQueryWrapper<RoleApp> rmRoleAppList = new LambdaQueryWrapper<>();
                rmRoleAppList.in(RoleApp::getAppId, notInNewAppIdList).and(wq -> wq.eq(RoleApp::getRoleId, dto.getId()));
                roleAppService.remove(rmRoleAppList);
            }
            List<Long> notInOldAppIdList = CollectionUtil.subtractToList(newAppIdList, oldAppIdList);
            log.info("授权的应用{}", JSONUtil.toJsonStr(notInOldAppIdList));
            List<RoleApp> addRoleAppList = new ArrayList<>();
            for (Long appId : notInOldAppIdList) {
                RoleApp roleApp = new RoleApp();
                roleApp.setAppId(appId);
                roleApp.setRoleId(dto.getId());
                addRoleAppList.add(roleApp);
            }
            roleAppService.saveBatch(addRoleAppList);

            List<Long> oldMenuIdList = menuInfoService.getMenuIdListByRoleId(dto.getId());
            log.info("原始的菜单{}", JSONUtil.toJsonStr(oldMenuIdList));
            List<Long> newMenuIdList = menuInfoList.stream().map(MenuInfo::getId).collect(Collectors.toList());
            newMenuIdList = cUtil.removeRepLong(newMenuIdList);
            log.info("新提交的菜单{}", JSONUtil.toJsonStr(newMenuIdList));
            List<Long> notInNewMenuIdList = CollectionUtil.subtractToList(oldMenuIdList, newMenuIdList);
            log.info("原始中有，新提交没有的菜单{}", JSONUtil.toJsonStr(notInNewMenuIdList));
            log.info("回收的菜单{}", JSONUtil.toJsonStr(notInNewMenuIdList));
            if (CollectionUtil.isNotEmpty(notInNewMenuIdList)) {
                LambdaQueryWrapper<RoleMenu> rmRoleMenu = new LambdaQueryWrapper<>();
                rmRoleMenu.in(RoleMenu::getMenuId, notInNewMenuIdList).and(wq -> wq.eq(RoleMenu::getRoleId, dto.getId()));
                roleMenuService.remove(rmRoleMenu);
            }
            List<Long> notInOldMenuIdList = CollectionUtil.subtractToList(newMenuIdList, oldMenuIdList);
            log.info("新提交有，原始中没有的菜单{}", JSONUtil.toJsonStr(notInOldMenuIdList));
            log.info("授权的菜单{}", JSONUtil.toJsonStr(notInOldMenuIdList));
            List<RoleMenu> addRoleMenu = new ArrayList<>();
            for (Long menuId : notInOldMenuIdList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(dto.getId());
                roleMenu.setMenuId(menuId);
                addRoleMenu.add(roleMenu);
            }
            roleMenuService.saveBatch(addRoleMenu);
        }
        List<Long> oldOrgIdList = orgInfoService.getOrgIdListByRoleId(dto.getId());
        log.info("原始的组织{}", JSONUtil.toJsonStr(oldOrgIdList));
        List<Long> newOrgIdList = dto.getOrgIds();
        log.info("新提交的组织{}", JSONUtil.toJsonStr(newOrgIdList));
        List<Long> notInNewOrgIdList = CollectionUtil.subtractToList(oldOrgIdList, newOrgIdList);
        log.info("原始中有，新提交没有的组织{}", JSONUtil.toJsonStr(notInNewOrgIdList));
        log.info("回收的组织{}", JSONUtil.toJsonStr(notInNewOrgIdList));
        if (CollectionUtil.isNotEmpty(notInNewOrgIdList)) {
            LambdaQueryWrapper<RoleOrg> rmRoleOrg = new LambdaQueryWrapper<>();
            rmRoleOrg.in(RoleOrg::getOrgId, notInNewOrgIdList).and(wq -> wq.eq(RoleOrg::getRoleId, dto.getId()));
            roleOrgService.remove(rmRoleOrg);
        }
        List<Long> notInOldOrgIdList = CollectionUtil.subtractToList(newOrgIdList, oldOrgIdList);
        log.info("新提交有，原始中没有的组织{}", JSONUtil.toJsonStr(notInOldOrgIdList));
        log.info("授权的组织{}", JSONUtil.toJsonStr(notInOldOrgIdList));

        List<RoleOrg> addRoleOrg = new ArrayList<>();
        for (Long orgId : notInOldOrgIdList) {
            RoleOrg roleOrg = new RoleOrg();
            roleOrg.setRoleId(dto.getId());
            roleOrg.setOrgId(orgId);
            addRoleOrg.add(roleOrg);
        }
        roleOrgService.saveBatch(addRoleOrg);

        List<Long> oldAreaIdList = videoAreaService.getAreaIdListByRoleId(dto.getId());
        log.info("原始的区域{}", JSONUtil.toJsonStr(oldAreaIdList));
        List<Long> newAreaIdList = dto.getAreaIds();
        log.info("新提交的区域{}", JSONUtil.toJsonStr(newAreaIdList));
        List<Long> notInNewAreaIdList = CollectionUtil.subtractToList(oldAreaIdList, newAreaIdList);
        log.info("原始中有，新提交没有的区域{}", JSONUtil.toJsonStr(notInNewAreaIdList));
        log.info("回收的区域{}", JSONUtil.toJsonStr(notInNewAreaIdList));
        if (CollectionUtil.isNotEmpty(notInNewAreaIdList)) {
            LambdaQueryWrapper<RoleArea> rmRoleArea = new LambdaQueryWrapper<>();
            rmRoleArea.in(RoleArea::getAreaId, notInNewAreaIdList).and(wq -> wq.in(RoleArea::getRoleId, dto.getId()));
            roleAreaService.remove(rmRoleArea);
        }
        List<Long> notInOldAreaIdList = CollectionUtil.subtractToList(newAreaIdList, oldAreaIdList);
        log.info("新提交有，原始中没有的区域{}", JSONUtil.toJsonStr(notInOldAreaIdList));
        log.info("授权的区域{}", JSONUtil.toJsonStr(notInOldAreaIdList));
        List<RoleArea> addRoleArea = new ArrayList<>();
        for (Long areaId : notInOldAreaIdList) {
            RoleArea roleArea = new RoleArea();
            roleArea.setRoleId(dto.getId());
            roleArea.setAreaId(areaId);
            addRoleArea.add(roleArea);
        }
        roleAreaService.saveBatch(addRoleArea);
    }

    @Override
    public Page<SysRoleInfoVO> findByPage(QuerySysRoleInfoDTO dto) {
        PageSysRoleInfoDto page = new PageSysRoleInfoDto();
        if (dto.getRoleName() != null) {
            page.setRoleName(dto.getRoleName());
        }
        if (dto.getUserAccount() != null) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (dto.getCreatedTimeStart() != null) {
            page.setCreatedTimeStart(dto.getCreatedTimeStart());
        }
        if (dto.getCreatedTimeEnd() != null) {
            page.setCreatedTimeEnd(dto.getCreatedTimeEnd());
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
        return roleInfoMapper.MySelectPage(page);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getRoleId, id);
        roleUserService.remove(queryWrapper);
        roleInfoMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void erasureBatch(List<Long> ids) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleUser::getRoleId, ids);
        roleUserService.remove(queryWrapper);
        roleInfoMapper.deleteBatchIds(ids);
    }

    @Transactional
    @Override
    public void modifyByStatus(StatusSysRoleInfoDTO dto) {
        RoleInfo roleInfo = roleInfoMapper.selectById(dto.getId());
        roleInfo.setStatus(dto.getStatus());
        roleInfoMapper.updateById(roleInfo);
    }

    @Override
    public Page<EditUserSysRoleInfoVO> getEditUserSysRoleInfoList(QueryEditUserSysRoleInfoDTO dto) {
        PageEditUserSysRoleInfoDTO page = new PageEditUserSysRoleInfoDTO();
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

        return roleInfoMapper.selectEditUserSysRoleInfoPage(page);
    }

    @Override
    public RoleDetailVO getRoleDetailById(Long id) {
        // 返回实体
        RoleDetailVO roleDetailVO = new RoleDetailVO();
        // 查询角色基本信息
        RoleInfo roleInfo = roleInfoMapper.selectById(id);
        if (null == roleInfo) {
            throw new BusinessException(BusinessErrorEnums.VALID_NO_OBJECT_FOUND);
        }
        roleDetailVO.setId(roleInfo.getId());
        roleDetailVO.setRoleName(roleInfo.getRoleName());
        roleDetailVO.setRoleDesc(roleInfo.getRoleDesc());

        // 查询该角色已授权的应用列表
        List<AppInfo> appInfoList = appInfoService.getAppByRoleCode(roleInfo.getRoleCode());
        // 查询该角色已授权的菜单列表
        List<MenuInfo> menuInfoList = menuInfoService.getMenuByRoleCode(roleInfo.getRoleCode());
        // 查询该角色已授权的接口列表
        List<ApiInfo> apiInfoList = apiInfoService.getApiInfoByRoleCode(roleInfo.getRoleCode());

        // 应用类
        List<String> appIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, AppTypeConstant.FUNC);
        appIds = appIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setAppIds(appIds);
        // 配置类
        List<String> configIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, AppTypeConstant.CONF);
        configIds = configIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setConfigIds(configIds);
        // 运维类
        List<String> devopsIds = getAppMenuApi(appInfoList, menuInfoList, apiInfoList, AppTypeConstant.DEV);
        devopsIds = devopsIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setDevopsIds(devopsIds);

        // 查询该角色已授权的部门列表
        List<OrgInfo> orgList = orgInfoService.getOrgInfoByRoleCode(roleInfo.getRoleCode());
        List<String> orgIds = orgList.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
        orgIds = orgIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setOrgIds(orgIds);
        // 查询该角色已授权的安防区域
        List<VideoArea> areaList = videoAreaService.getVideoAreaByRoleCode(roleInfo.getRoleCode());
        List<String> areaIds = areaList.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
        areaIds = areaIds.stream().distinct().collect(Collectors.toList());
        roleDetailVO.setAreaIds(areaIds);
        return roleDetailVO;
    }


    @Override
    public List<Tree<Long>> getAppMenuApiTree(Integer appType) {
        // 根据类型查出 涉及的应用并取出appIds
        LambdaQueryWrapper<AppInfo> appInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        appInfoLambdaQueryWrapper.eq(AppInfo::getAppType, appType);
        List<AppInfo> appInfoList = appInfoService.list(appInfoLambdaQueryWrapper);
        List<Long> appIds = appInfoList.stream().map(AppInfo::getId).collect(Collectors.toList());
        // 补充系统内置根节点
        appIds.add(0L);
        // 根据应用 appIds,查取菜单
        LambdaQueryWrapper<MenuInfo> menuInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuInfoLambdaQueryWrapper.in(MenuInfo::getAppId, appIds);
        // 角色管理，权限配置，系统权限暂时不显示按钮就别
        menuInfoLambdaQueryWrapper.ne(MenuInfo::getMenuType, 3);
        // 隐藏状态下的菜单不参与授权
//        menuInfoLambdaQueryWrapper.ne(MenuInfo::getHidden, 1);
        List<MenuInfo> menuInfoList = menuInfoService.list(menuInfoLambdaQueryWrapper);
        List<AppMenuApiVO> vos = new ArrayList<>();
        for (MenuInfo menuInfo : menuInfoList) {
            AppMenuApiVO vo = new AppMenuApiVO();
            vo.setId(menuInfo.getId());
            switch (menuInfo.getMenuType()) {
                case 1: {
                    vo.setIdStr("A_" + menuInfo.getId());
                    break;
                }
                case 2: {
                    vo.setIdStr("M_" + menuInfo.getId());
                    break;
                }
                case 3: {
                    vo.setIdStr("U_" + menuInfo.getId());
                    break;
                }
                default: {
                    vo.setIdStr("");
                    break;
                }
            }
            vo.setPid(menuInfo.getMenuPid());
            vo.setName(menuInfo.getTitle());
            vos.add(vo);
        }
        List<AppMenuApiVO> treeVos = vos.stream().distinct().collect(Collectors.toList());
        List<AppMenuApiTree> appMenuApiTreeList = treeVos.stream().map(item -> {
            AppMenuApiTree vo = new AppMenuApiTree();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        appMenuApiTreeList.forEach(e -> {
            TreeNode<Long> treeNode = new TreeNode<>(e.getId(), e.getParentId(), e.getName(), null);
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("idStr", e.getIdStr());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        });
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig() {{
            setIdKey("id");
            setNameKey("name");
            setParentIdKey("pid");
            setChildrenKey("children");
        }};
        return TreeUtil.build(nodeList, 0L, treeNodeConfig, new DefaultNodeParser<>());
    }

    @Transactional
    @Override
    public void addRelationUser(RoleRelationUserDTO dto) {
        // 1.根据角色ID查取以往关联的用户列表
        List<Long> oldUserIds = userInfoService.getUserIdListByRoleId(dto.getRoleId());
        // 如果旧关联为空，则本次为新关联
        if (CollUtil.isEmpty(oldUserIds)) {
            List<RoleUser> roleUserList = new ArrayList<>();
            for (Long userId : dto.getUserIdList()) {
                RoleUser roleUser = new RoleUser();
                roleUser.setRoleId(dto.getRoleId());
                roleUser.setUserId(userId);
                roleUserList.add(roleUser);
            }
            roleUserService.saveBatch(roleUserList);
            return;
        }
        // 如果新关联为空，则是取消关联
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            //
            LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RoleUser::getRoleId, dto.getRoleId());
            roleUserService.remove(queryWrapper);
            return;
        }
        // 2.求取新旧的相同点
        List<Long> commonUserId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 原始应用列表剔除相同部分后进行删除
        oldUserIds.retainAll(commonUserId);
        if (CollectionUtil.isNotEmpty(oldUserIds)) {
            LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(RoleUser::getUserId, oldUserIds).and(wq -> wq.eq(RoleUser::getRoleId, dto.getRoleId()));
            roleUserService.remove(queryWrapper);
        }
        // 新提交的应用列表剔除相同部分后新增授权
        List<RoleUser> roleUserList = new ArrayList<>();
        dto.getUserIdList().removeAll(commonUserId);
        for (Long userId : dto.getUserIdList()) {
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(dto.getRoleId());
            roleUser.setUserId(userId);
            roleUserList.add(roleUser);
        }
        roleUserService.saveBatch(roleUserList);
    }

    @Override
    public Page<RelationSysUserInfoVO> listRelationUser(QueryRoleRelationSysUserInfoDTO dto) {
        PageRoleRelationSysUserInfoDTO page = new PageRoleRelationSysUserInfoDTO();

        if (null != dto.getUserAccount() && !"".equals(dto.getUserAccount())) {
            page.setUserAccount(dto.getUserAccount());
        }
        if (null != dto.getRoleId()) {
            page.setRoleId(dto.getRoleId());
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
        return roleInfoMapper.relationSysUserInfoPage(page);

    }

    @Transactional
    @Override
    public void rightRelationUser(RoleRelationUserDTO dto) {
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            return;
        }
        List<Long> oldUserIds = userInfoService.getUserIdListByRoleId(dto.getRoleId());
        if (CollUtil.isEmpty(oldUserIds)) {
            return;
        }
        List<Long> commonId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 将相同部分后进行删除
        if (CollectionUtil.isNotEmpty(commonId)) {
            LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(RoleUser::getUserId, commonId).and(qw -> qw.eq(RoleUser::getRoleId, dto.getRoleId()));
            roleUserService.remove(queryWrapper);
        }
    }

    @Transactional
    @Override
    public void leftRelationUser(RoleRelationUserDTO dto) {
        if (CollUtil.isEmpty(dto.getUserIdList())) {
            return;
        }
        List<Long> oldUserIds = userInfoService.getUserIdListByRoleId(dto.getRoleId());
        if (CollUtil.isEmpty(oldUserIds)) {
            List<RoleUser> roleUserList = new ArrayList<>();
            for (Long userId : dto.getUserIdList()) {
                RoleUser roleUser = new RoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(dto.getRoleId());
                roleUserList.add(roleUser);
            }
            roleUserService.saveBatch(roleUserList);
            return;
        }
        // 求取新旧的相同点
        List<Long> commonId = oldUserIds.stream().filter(dto.getUserIdList()::contains).collect(Collectors.toList());
        // 新提交的应用列表剔除相同部分后新增授权
        dto.getUserIdList().removeAll(commonId);
        List<RoleUser> roleUserList = new ArrayList<>();
        for (Long userId : dto.getUserIdList()) {
            RoleUser roleUser = new RoleUser();
            roleUser.setRoleId(dto.getRoleId());
            roleUser.setUserId(userId);
            roleUserList.add(roleUser);
        }
        roleUserService.saveBatch(roleUserList);
    }

    private List<String> getAppMenuApi(List<AppInfo> appInfoList, List<MenuInfo> menuInfoList, List<ApiInfo> apiInfoList, Integer appType) {

        // 1.根据 appType 筛选出符合要求的应用
        List<AppInfo> myAppInfoList = new ArrayList<>();
        for (AppInfo appInfo : appInfoList) {
            if (appType.equals(appInfo.getAppType())) {
                myAppInfoList.add(appInfo);
            }
        }
        // 2-1. 将筛选后的应用列表作为筛选菜单的条件
        List<MenuInfo> myMenuInfoList = new ArrayList<>();
        for (AppInfo appInfo : myAppInfoList) {
            for (MenuInfo menuInfo : menuInfoList) {
                if (menuInfo.getId().equals(1L)) {
                    continue;
                }
                if (appInfo.getId().equals(menuInfo.getAppId())) {
                    myMenuInfoList.add(menuInfo);
                }
            }
        }
        // 3. 将筛选后的应用列表作为筛选接口的条件
        List<ApiInfo> myApiInfoList = new ArrayList<>();
        for (AppInfo appInfo : myAppInfoList) {
            for (ApiInfo apiInfo : apiInfoList) {
                if (apiInfo.getId().equals(1L)) {
                    continue;
                }
                if (appInfo.getId().equals(apiInfo.getAppId())) {
                    myApiInfoList.add(apiInfo);
                }
            }
        }

        // 4.拼接返回结果
        List<String> resultList = new ArrayList<>();
        for (AppInfo appInfo : myAppInfoList) {
            resultList.add("A_" + appInfo.getId());
        }
        for (MenuInfo menuInfo : myMenuInfoList) {
            resultList.add("M_" + menuInfo.getId());
        }
        for (ApiInfo apiInfo : myApiInfoList) {
            resultList.add("U_" + apiInfo.getId());
        }
        return resultList;
    }

    @Transactional
    @Override
    public void saveRoleUser(Long roleId, Long userId) {
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(userId);
        roleUser.setRoleId(roleId);
        roleUserService.save(roleUser);
    }

    @Override
    public List<Long> getRoleByUserId(Long userId) {
        LambdaQueryWrapper<RoleUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleUser::getUserId, userId);
        return roleUserService.list(lambdaQueryWrapper).stream().map(RoleUser::getRoleId).collect(Collectors.toList());
    }

    @Override
    public void removeRoleUser(Long roleId, Long userId) {
        LambdaQueryWrapper<RoleUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleUser::getRoleId, roleId);
        queryWrapper.eq(RoleUser::getUserId, userId);
        roleUserService.remove(queryWrapper);
    }

    @Override
    public List<String> getRoleCodeByUserId(Long userId) {
        return roleInfoMapper.selectRoleCodeByUserId(userId);
    }
}
