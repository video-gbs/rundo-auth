package com.runjian.rbac.domain.vo.tree;

import com.runjian.rbac.domain.vo.system.AppMenuApiVO;
import com.runjian.rbac.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AppMenuApiTree
 * @Description
 * @date 2023-02-15 周三 17:15
 */
public class AppMenuApiTree extends AppMenuApiVO implements DataTree<AppMenuApiTree> {

    List<AppMenuApiTree> children;

    @Override
    public Long getParentId() {
        return super.getPid();
    }

    @Override
    public void setChildren(List<AppMenuApiTree> children) {
        this.children = children;
    }

    @Override
    public List<AppMenuApiTree> getChildren() {
        return this.children;
    }
}
