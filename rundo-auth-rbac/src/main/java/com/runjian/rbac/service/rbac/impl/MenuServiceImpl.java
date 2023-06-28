package com.runjian.rbac.service.rbac.impl;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.MarkConstant;
import com.runjian.rbac.constant.MenuType;
import com.runjian.rbac.dao.MenuMapper;
import com.runjian.rbac.dao.relation.RoleMenuMapper;
import com.runjian.rbac.entity.MenuInfo;
import com.runjian.rbac.service.rbac.DataBaseService;
import com.runjian.rbac.service.rbac.MenuService;
import com.runjian.rbac.vo.AbstractTreeInfo;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public GetMenuTreeRsp getMenuList(String name, String path) {
        List<GetMenuTreeRsp> getMenuTreeRspList = menuMapper.selectByNameLikeAndPathLike(name, path);
        GetMenuTreeRsp rootMenuTree = GetMenuTreeRsp.getRootMenuTree();
        if (getMenuTreeRspList.size() == 0){
            return rootMenuTree;
        }
        if(Objects.nonNull(name) || Objects.nonNull(path)){
            Set<String> levels = getMenuTreeRspList.stream().map(AbstractTreeInfo::getLevel).collect(Collectors.toSet());

            Set<Long> ids = new HashSet<>();
            for (String level : levels){
                ids.addAll(Arrays.stream(level.split(MarkConstant.MARK_SPLIT_RAIL)).map(Long::parseLong).toList());
            }
            ids.remove(rootMenuTree.getId());
            if (ids.size() == 0){
                rootMenuTree.setChildList(getMenuTreeRspList);
                return rootMenuTree;
            }
            List<GetMenuTreeRsp> pMenuTreeRspList = menuMapper.selectAllByIds(ids);
            pMenuTreeRspList.addAll(getMenuTreeRspList);
            rootMenuTree.setChildList(rootMenuTree.recursionData(pMenuTreeRspList, rootMenuTree.getLevel()));
            return rootMenuTree;
        }
        rootMenuTree.setChildList(rootMenuTree.recursionData(getMenuTreeRspList, rootMenuTree.getLevel()));
        return rootMenuTree;
    }


    @Override
    public void addMenu(Long menuPid, Integer menuSort, Integer menuType, String path, String component, String name, String icon, String description, Integer hidden, Integer disabled) {
        MenuInfo pMenuInfo;
        if (menuPid.equals(0L)){
            pMenuInfo = new MenuInfo();
            pMenuInfo.setLevelNum(0);
            pMenuInfo.setLevel("0");
            pMenuInfo.setMenuType(1);
        }else {
            pMenuInfo = dataBaseService.getMenuInfo(menuPid);
        }

        if (pMenuInfo.getMenuType().equals(MenuType.ABSTRACT.getCode()) && !menuType.equals(MenuType.ABSTRACT.getCode()) ){
            throw new BusinessException(BusinessErrorEnums.VALID_ILLEGAL_OPERATION, "不能在抽象的菜单下创建非抽象的菜单");
        }
        LocalDateTime nowTime = LocalDateTime.now();
        MenuInfo cMenuInfo = new MenuInfo();
        cMenuInfo.setMenuPid(menuPid);
        cMenuInfo.setSort(menuSort);
        cMenuInfo.setMenuType(menuType);
        cMenuInfo.setPath(path);
        cMenuInfo.setComponent(component);
        cMenuInfo.setName(name);
        cMenuInfo.setIcon(icon);
        cMenuInfo.setDescription(description);
        if (menuPid.equals(0L)){
            cMenuInfo.setLevel(pMenuInfo.getLevel());
        }else {
            cMenuInfo.setLevel(pMenuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pMenuInfo.getId());
        }
        cMenuInfo.setLevelNum(pMenuInfo.getLevelNum() + 1);
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
            MenuInfo pMenuInfo;
            cMenuInfo.setMenuPid(menuPid);
            if (menuPid.equals(0L)){
                cMenuInfo.setLevel("0");
                cMenuInfo.setLevelNum(1);
            }else {
                pMenuInfo = dataBaseService.getMenuInfo(menuPid);
                cMenuInfo.setLevel(pMenuInfo.getLevel() + MarkConstant.MARK_SPLIT_RAIL + pMenuInfo.getId());
                cMenuInfo.setLevelNum(pMenuInfo.getLevelNum() + 1);
            }
        }
        cMenuInfo.setSort(menuSort);
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
