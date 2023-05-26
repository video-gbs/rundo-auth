package com.runjian.rbac.domain.vo.tree;

import com.runjian.rbac.domain.vo.system.SysOrgVO;
import com.runjian.rbac.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgNode
 * @Description 树形
 * @date 2023-01-12 周四 11:46
 */
public class SysOrgTree extends SysOrgVO implements DataTree<SysOrgTree> {

    private List<SysOrgTree> children;

    @Override
    public Long getParentId() {
        return super.getOrgPid();
    }

    @Override
    public void setChildren(List<SysOrgTree> children) {
        this.children = children;
    }

    @Override
    public List<SysOrgTree> getChildren() {
        return this.children;
    }
}
