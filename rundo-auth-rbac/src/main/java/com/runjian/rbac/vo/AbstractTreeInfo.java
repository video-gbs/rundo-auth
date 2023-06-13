package com.runjian.rbac.vo;

import com.runjian.common.constant.MarkConstant;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/13 10:06
 */
@Data
public abstract class AbstractTreeInfo {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 层级
     */
    private String level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 孩子节点
     */
    private List<? extends AbstractTreeInfo> childList;

    /**
     * 获取子节点
     * @param treeInfoList
     * @param level
     * @return
     */
    public static List<? extends AbstractTreeInfo> recursionData(List<? extends AbstractTreeInfo> treeInfoList, String level){
        List<? extends AbstractTreeInfo> next = treeInfoList.stream().filter(menuInfo -> menuInfo.getLevel().equals(level)).toList();

        for (AbstractTreeInfo treeNode : next){
            List<? extends AbstractTreeInfo> menuTreeRspList = treeInfoList.stream()
                    .filter(node -> node.getLevel().startsWith(treeNode.getLevel() + MarkConstant.MARK_SPLIT_RAIL + treeNode.getId())).toList();
            treeNode.setChildList(recursionData(menuTreeRspList, treeNode.getLevel() + MarkConstant.MARK_SPLIT_RAIL + treeNode.getId()));
        }
        return next.stream().sorted(Comparator.comparing(AbstractTreeInfo::getSort)).toList();
    }
}
