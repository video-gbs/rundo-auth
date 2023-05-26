package com.runjian.rbac.domain.vo.tree;

import com.runjian.rbac.domain.vo.system.SysApiInfoVO;
import com.runjian.rbac.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysApiInfoTree
 * @Description 接口树
 * @date 2023-01-31 周二 16:06
 */
public class ApiInfoTree extends SysApiInfoVO implements DataTree<ApiInfoTree> {

    List<ApiInfoTree> children;

    @Override
    public Long getParentId() {
        return super.getApiPid();
    }

    @Override
    public void setChildren(List<ApiInfoTree> children) {
        this.children = children;
    }

    @Override
    public List<ApiInfoTree> getChildren() {
        return this.children;
    }
}
