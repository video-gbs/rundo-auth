package com.runjian.rbac.service.system.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.runjian.rbac.constant.DefaultConstant;
import com.runjian.rbac.domain.dto.system.HiddenChangeDTO;
import com.runjian.rbac.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusChangeDTO;
import com.runjian.rbac.domain.dto.system.SysMenuInfoDTO;
import com.runjian.rbac.domain.entity.AppInfo;
import com.runjian.rbac.domain.entity.MenuInfo;
import com.runjian.rbac.domain.entity.RoleMenu;
import com.runjian.rbac.domain.vo.system.MenuInfoVO;
import com.runjian.rbac.domain.vo.system.MyMetaClass;
import com.runjian.rbac.domain.vo.tree.MenuInfoTree;
import com.runjian.rbac.mapper.MenuInfoMapper;
import com.runjian.rbac.service.system.AppInfoService;
import com.runjian.rbac.service.system.MenuInfoService;
import com.runjian.rbac.service.system.RoleMenuService;
import com.runjian.rbac.util.RoleIdUtil;
import com.runjian.rbac.util.tree.DataTreeUtil;
import com.runjian.common.config.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:45:53
 */
@Slf4j
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfo> implements MenuInfoService {
    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Lazy
    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleIdUtil roleIdUtil;

    @Override
    public List<Tree<Long>> findByTree(QuerySysMenuInfoDTO dto) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(MenuInfo::getMenuType, 3);
        if (null != dto.getAppId()) {
            queryWrapper.eq(MenuInfo::getAppId, dto.getAppId());
        }
        if (null != dto.getAppName() && !"".equals(dto.getAppName())) {
            queryWrapper.like(MenuInfo::getAppName, dto.getAppName());
        }
        if (null != dto.getMenuName() && !"".equals(dto.getMenuName())) {
            queryWrapper.like(MenuInfo::getTitle, dto.getMenuName());
        }
        if (null != dto.getUrl() && !"".equals(dto.getUrl())) {
            queryWrapper.like(MenuInfo::getPath, dto.getUrl());
        }
        List<MenuInfo> menuInfoList = menuInfoMapper.selectList(queryWrapper);
        long rootNodeId = 1L;
        int flag = 0;
        for (MenuInfo info : menuInfoList) {
            if (info.getId() == rootNodeId) {
                flag = flag + 1;
            }
        }
        if (flag == 0) {
            // 此处可以通过写死模拟一个根节点
            MenuInfo menuInfo = menuInfoMapper.selectById(1);
            menuInfoList.add(menuInfo);
        }
        List<MenuInfoTree> menuInfoTreeList = menuInfoList.stream().map(item -> {
            MenuInfoTree bean = new MenuInfoTree();
            BeanUtils.copyProperties(item, bean);
            bean.setMeta(new MyMetaClass(item.getTitle(), item.getIcon()));
            return bean;
        }).collect(Collectors.toList());
        return getTree(menuInfoTreeList);
    }

    @Override
    public List<MenuInfoTree> findByTreeByAppType(Integer appType) {
        // 取出当前登录的用户的所有角色
        List<String> roleCodeList = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        List<Long> roleIds = roleIdUtil.getRoleIdList();
        // 根据角色获取用户应该有的应用ID，剔除不是appType的应用
        List<AppInfo> appInfoList = CollUtil.distinct(appInfoService.getAppByRoleCodelist(roleCodeList));
        appInfoList.removeIf(appInfo -> !appInfo.getAppType().equals(appType));
        List<Long> appIds = appInfoList.stream().map(AppInfo::getId).collect(Collectors.toList());
        // 根据角色获取用户已有的菜单ID
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.in(RoleMenu::getRoleId, roleIds);
        List<Long> menuIds = roleMenuService.list(roleMenuLambdaQueryWrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        menuIds.stream().distinct();
        List<MenuInfoTree> menuInfoTreeByAppTypelist = new ArrayList<>();
        for (Long appId : appIds) {
            List<MenuInfo> menuInfoList1 = menuInfoMapper.selectMenuList(appId, menuIds);
            Set<MenuInfo> set = new HashSet<>(menuInfoList1);
            List<MenuInfo> menuInfoList = new ArrayList<>(set);
            long rootId = 0L;
            for (MenuInfo menuInfo : menuInfoList) {
                if (menuInfo.getMenuPid().equals(DefaultConstant.ID) && menuInfo.getLevel().equals(2)) {
                    rootId = menuInfo.getId();
                }
            }
            menuInfoTreeByAppTypelist.addAll(DataTreeUtil.buildTree(menuInfoList.stream().map(item -> {
                MenuInfoTree bean = new MenuInfoTree();
                BeanUtils.copyProperties(item, bean);
                bean.setMeta(new MyMetaClass(item.getTitle(), item.getIcon()));
                return bean;
            }).collect(Collectors.toList()), rootId));
        }
        menuInfoTreeByAppTypelist.stream().distinct();
        return menuInfoTreeByAppTypelist;
    }

    @Transactional
    @Override
    public void modifyByStatus(StatusChangeDTO dto) {
        MenuInfo menuInfo = menuInfoMapper.selectById(dto.getId());
        menuInfo.setStatus(dto.getStatus());
        menuInfoMapper.updateById(menuInfo);
    }

    @Transactional
    @Override
    public void modifyByHidden(HiddenChangeDTO dto) {
        MenuInfo menuInfo = menuInfoMapper.selectById(dto.getId());
        menuInfo.setHidden(dto.getHidden());
        menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public List<Tree<Long>> getTreeByAppId(Long appId) {
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuInfo::getAppId, appId);
        AtomicLong root = new AtomicLong(0L);
        List<MenuInfo> menuInfoList = menuInfoMapper.selectList(queryWrapper);
        List<MenuInfoTree> menuInfoTreeList = menuInfoList.stream().map(
                item -> {
                    if (item.getAppId().longValue() == appId && item.getMenuPid() == 1L) {
                        root.set(item.getId());
                    }
                    MenuInfoTree bean = new MenuInfoTree();
                    BeanUtils.copyProperties(item, bean);
                    bean.setMeta(new MyMetaClass(item.getTitle(), item.getIcon()));
                    return bean;
                }
        ).collect(Collectors.toList());
        return getTree(menuInfoTreeList);
    }

    @Override
    public List<MenuInfo> getMenuByRoleCode(String roleCode) {
        return menuInfoMapper.selectMenuByRoleCode(roleCode);
    }

    @Override
    public List<Long> getMenuIdListByRoleId(Long roleId) {
        return menuInfoMapper.findMenuIdListByRoleId(roleId);
    }

    @Transactional
    @Override
    public void save(SysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(dto, menuInfo);
        MenuInfo parentInfo = menuInfoMapper.selectById(dto.getMenuPid());
        String menuPids = parentInfo.getMenuPids() + "[" + dto.getMenuPid() + "]";
        menuInfo.setMenuPids(menuPids);
        menuInfo.setParentName(parentInfo.getTitle());
        if (null == dto.getMenuSort()) {
            menuInfo.setMenuSort(100);
        }
        menuInfo.setMenuSort(dto.getMenuSort());
        menuInfo.setLeaf(0);
        menuInfo.setLevel(parentInfo.getLevel() + 1);
        menuInfoMapper.insert(menuInfo);
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setMenuId(menuInfo.getId());
        roleMenu.setRoleId(1L);
        roleMenuService.save(roleMenu);
    }

    @Transactional
    @Override
    public void erasureById(Long id) {
        // 1.确认当前需要删除的菜单有无下级菜单
        LambdaQueryWrapper<MenuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(MenuInfo::getMenuPids, "[" + id + "]");
        List<MenuInfo> menuInfoChild = menuInfoMapper.selectList(queryWrapper);
        if (menuInfoChild.size() > 0) {
            // 1.1 有下级菜单不允许删除
            throw new BusinessException("不能删除含有下级菜单的菜单");
        }
        // 1.2 无下级菜单才可以删除
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getMenuId, id);
        roleMenuService.remove(wrapper);
        menuInfoMapper.deleteById(id);
    }

    @Transactional
    @Override
    public void modifyById(SysMenuInfoDTO dto) {
        MenuInfo menuInfo = new MenuInfo();
        BeanUtils.copyProperties(dto, menuInfo);
        menuInfoMapper.updateById(menuInfo);
    }

    @Override
    public MenuInfoVO findById(Long id) {
        MenuInfo menuInfo = menuInfoMapper.selectById(id);
        MenuInfoVO menuInfoVO = new MenuInfoVO();
        BeanUtils.copyProperties(menuInfo, menuInfoVO);
        MenuInfo parentMenuInfo = menuInfoMapper.selectById(menuInfo.getMenuPid());
        menuInfoVO.setParentName(parentMenuInfo.getTitle());
        return menuInfoVO;
    }

    private List<Tree<Long>> getTree(List<MenuInfoTree> menuInfoTreeList) {
        List<TreeNode<Long>> nodeList = new ArrayList<>();
        menuInfoTreeList.forEach(e -> {
            TreeNode<Long> treeNode = new TreeNode<>(e.getId(), e.getParentId(), e.getTitle(), e.getMenuSort());
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("appId", e.getAppId());
            extraMap.put("appName", e.getAppName());
            extraMap.put("icon", e.getIcon());
            extraMap.put("path", e.getPath());
            extraMap.put("name", e.getName());
            extraMap.put("menuType", e.getMenuType());
            extraMap.put("level", e.getLevel());
            extraMap.put("component", e.getComponent());
            extraMap.put("status", e.getStatus());
            extraMap.put("hidden", e.getHidden());
            extraMap.put("redirect", e.getRedirect());
            extraMap.put("meta", e.getMeta());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        });
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig() {{
            setIdKey("id");
            setNameKey("title");
            setParentIdKey("menuPid");
            setChildrenKey("children");
            setWeightKey("menuSort");
        }};
        return TreeUtil.build(nodeList, 0L, treeNodeConfig, new DefaultNodeParser<>());
    }

}
