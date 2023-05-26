package com.runjian.rbac.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName DataTreeUtil
 * @Description 构造树形结构工具
 * @date 2023-01-10 周二 17:18
 */
public class DataTreeUtil {

    public static <T extends DataTree<T>> List<T> buildTree(List<T> paramList, Long rootNodeId) {
        List<T> returnList = new ArrayList<>();
        for (T node : paramList) {
            // 查找根节点
            if (node.getId().longValue() == rootNodeId.longValue()) {
                returnList.add(node);
            }
        }
        //递归为children赋值
        for (T entry : paramList) {
            toTreeChildren(returnList, entry);
        }
        return returnList;
    }

    private static <T extends DataTree<T>> void toTreeChildren(List<T> returnList, T entry) {
        for (T node : returnList) {
            if (entry.getParentId().longValue() == node.getId().longValue()) {
                if (node.getChildren() == null) {
                    node.setChildren(new ArrayList<>());
                }
                node.getChildren().add(entry);
            }
            if (node.getChildren() != null) {
                toTreeChildren(node.getChildren(), entry);
            }
        }
    }
}
