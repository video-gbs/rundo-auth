package com.runjian.rbac.service.impl;

import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.dao.MenuMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.entity.MenuInfo;
import com.runjian.rbac.service.DataBaseService;
import com.runjian.rbac.service.MenuService;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Miracle
 * @date 2023/6/5 11:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    private final RoleMenuMapper roleMenuMapper;

    private final DataBaseService dataBaseService;


    @Override
    public List<GetMenuTreeRsp> getMenuList(String name, String path) {
        if (Objects.nonNull(name) || Objects.nonNull(path)){
            List<GetMenuTreeRsp> getMenuTreeRspList = menuMapper.selectByNameLikeAndPathLike(name, path);
            List<GetMenuTreeRsp> rootMenuTreeList = getMenuTreeRspList.stream().filter(getMenuTreeRsp -> {
                if (getMenuTreeRsp.getLevel().equals("0")) {
                    return true;
                }
                for (GetMenuTreeRsp child : getMenuTreeRspList) {
                    return !getMenuTreeRsp.getLevel().startsWith(child.getLevel() + MarkConstant.MARK_SPLIT_RAIL + child.getId());
                }
                return true;
            }).toList();
            rootMenuTreeList.forEach(root -> {
                String level = root.getLevel() + MarkConstant.MARK_SPLIT_RAIL + root.getId();
                root.setChildList(recursionData(menuMapper.selectAllByLevelLike(level), level));
            });
            return rootMenuTreeList;
        }else {
            GetMenuTreeRsp rootMenuTree = GetMenuTreeRsp.getRootMenuTree();
            rootMenuTree.setChildList(recursionData(menuMapper.selectAll(), rootMenuTree.getLevel()));
            return List.of(rootMenuTree);
        }
    }

    private List<GetMenuTreeRsp> recursionData(List<GetMenuTreeRsp> menuInfoList, String level){
        List<GetMenuTreeRsp> next = menuInfoList.stream().filter(menuInfo -> menuInfo.getLevel().equals(level)).toList();
        for (GetMenuTreeRsp getMenuTreeRsp : next){
            List<GetMenuTreeRsp> menuTreeRspList = menuInfoList.stream()
                    .filter(node -> node.getLevel().startsWith(getMenuTreeRsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + getMenuTreeRsp.getId())).toList();
            getMenuTreeRsp.setChildList(recursionData(menuTreeRspList, getMenuTreeRsp.getLevel() + MarkConstant.MARK_SPLIT_RAIL + getMenuTreeRsp.getId()));
        }
        return next.stream().sorted(Comparator.comparing(GetMenuTreeRsp::getMenuSort)).toList();
    }

    @Override
    public void addMenu(Long menuPid, Integer menuSort, Integer menuType, String path, String component, String name, String icon, String description, Integer hidden, Integer disabled) {
        MenuInfo pMenuInfo = dataBaseService.getMenuInfo(menuPid);
        LocalDateTime nowTime = LocalDateTime.now();
        MenuInfo cMenuInfo = new MenuInfo();
        cMenuInfo.setMenuPid(menuPid);
        cMenuInfo.setMenuSort(menuSort);
        cMenuInfo.setMenuType(menuType);
        cMenuInfo.setPath(path);
        cMenuInfo.setComponent(component);
        cMenuInfo.setName(name);
        cMenuInfo.setIcon(icon);
        cMenuInfo.setDescription(description);
        cMenuInfo.setLevel(pMenuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pMenuInfo.getId());
        cMenuInfo.setHidden(hidden);
        cMenuInfo.setDisabled(disabled);
        cMenuInfo.setCreateTime(nowTime);
        cMenuInfo.setUpdateTime(nowTime);
        menuMapper.save(cMenuInfo);
    }

    @Override
    public void updateDisabled(Long id, Integer disabled) {
        MenuInfo menuInfo = dataBaseService.getMenuInfo(id);
        menuInfo.setDisabled(disabled);
        menuInfo.setUpdateTime(LocalDateTime.now());
        menuMapper.updateDisabled(menuInfo);
    }

    @Override
    public void updateHidden(Long id, Integer hidden) {
        MenuInfo menuInfo = dataBaseService.getMenuInfo(id);
        menuInfo.setHidden(hidden);
        menuInfo.setUpdateTime(LocalDateTime.now());
        menuMapper.updateHidden(menuInfo);
    }

    @Override
    public void updateMenu(Long id, Long menuPid, Integer menuSort, Integer menuType, String path, String component, String name, String icon, String description, Integer hidden, Integer disabled) {
        MenuInfo cMenuInfo = dataBaseService.getMenuInfo(id);
        if (!cMenuInfo.getMenuPid().equals(menuPid)){
            MenuInfo pMenuInfo = dataBaseService.getMenuInfo(menuPid);
            cMenuInfo.setMenuPid(menuPid);
            cMenuInfo.setLevel(pMenuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pMenuInfo.getId());
        }
        cMenuInfo.setMenuSort(menuSort);
        cMenuInfo.setMenuType(menuType);
        cMenuInfo.setPath(path);
        cMenuInfo.setComponent(component);
        cMenuInfo.setName(name);
        cMenuInfo.setIcon(icon);
        cMenuInfo.setDescription(description);
        cMenuInfo.setHidden(hidden);
        cMenuInfo.setDisabled(disabled);
        cMenuInfo.setUpdateTime(LocalDateTime.now());
        menuMapper.update(cMenuInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        MenuInfo menuInfo = dataBaseService.getMenuInfo(id);
        Set<Long> menuIds = menuMapper.selectIdByPid(menuInfo.getId());
        menuIds.add(menuInfo.getId());
        roleMenuMapper.deleteAllByMenuIds(menuIds);
        menuMapper.deleteAll(menuIds);
    }
}
