package com.runjian.rbac.domain.vo.tree;

import com.runjian.rbac.domain.vo.system.MenuInfoVO;
import com.runjian.rbac.util.tree.DataTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysMenuTree
 * @Description 菜单树
 * @date 2023-01-31 周二 15:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuInfoTree extends MenuInfoVO implements DataTree<MenuInfoTree> {

    List<MenuInfoTree> children;

    @Override
    public Long getParentId() {
        return super.getMenuPid();
    }

    @Override
    public void setChildren(List<MenuInfoTree> children) {
        this.children = children;
    }

    @Override
    public List<MenuInfoTree> getChildren() {
        return this.children;
    }
}
