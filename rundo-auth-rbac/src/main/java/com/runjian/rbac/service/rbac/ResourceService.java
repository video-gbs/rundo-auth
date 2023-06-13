package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetResourceTreeRsp;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/6 10:52
 */
public interface ResourceService {


    /**
     * 获取分组资源树
     * @param resourceKey
     * @return
     */
    GetResourceTreeRsp getResourceTree(String resourceKey, Boolean isIncludeResource);


    /**
     * 批量添加资源
     * @param resourcePid 资源父id
     * @param resourceType 资源类型
     * @param resourceName 资源名称
     * @param resourceKey 资源Key
     * @param resourceValue 资源value数组
     */
    void batchAddResource(Long resourcePid, Integer resourceType, String resourceName, String resourceKey, Set<String> resourceValue);

    /**
     * 修改资源
     * @param resourceId 资源id
     * @param resourceName 资源名称
     * @param resourceValue 资源value
     */
    void updateResource(Long resourceId, String resourceName, String resourceValue);

    /**
     * 批量删除
     * @param resourceIds 资源id组
     */
    void batchDelete(Set<Long> resourceIds);

    /**
     * 父子移动节点
     * @param id 部门id
     * @param pid 父id
     */
    void fsMove(Long id, Long pid);

    /**
     * 兄弟移动节点
     * @param id 部门id
     * @param moveOp 1上移 0下移
     */
    void btMove(Long id, Integer moveOp);

}
