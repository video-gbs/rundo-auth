package com.runjian.rbac.domain.vo.tree;

import com.runjian.rbac.domain.vo.system.VideoAreaVO;
import com.runjian.rbac.util.tree.DataTree;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AreaNode
 * @Description 安保区域树形
 * @date 2023-01-13 周五 9:54
 */
public class VideoAreaTree extends VideoAreaVO implements DataTree<VideoAreaTree> {

    private List<VideoAreaTree> children;

    @Override
    public Long getParentId() {
        return super.getAreaPid();
    }

    @Override
    public void setChildren(List<VideoAreaTree> children) {
        this.children = children;
    }

    @Override
    public List<VideoAreaTree> getChildren() {
        return this.children;
    }
}
