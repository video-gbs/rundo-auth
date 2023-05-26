package com.runjian.rbac.util.tree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName DataTree
 * @Description 树形结构数据工具
 * @date 2023-01-10 周二 17:12
 */
public interface DataTree<T> {

    /**
     * 维护树形关系：元素一id
     */
    Long getId();

    /**
     * 维护树形关系：元素二 父id
     */
    Long getParentId();

    /**
     * 子节点数组
     *
     * @param children
     */
    void setChildren(List<T> children);

    List<T> getChildren();

}
