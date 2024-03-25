package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetResourceRootRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;

import java.util.List;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/6/6 10:52
 */
public interface ResourceService {

    /**
     * 获取分组资源树
     * @param resourceKey 资源key
     * @param isIncludeResource 是否包含资源
     * @return
     */
    GetResourceTreeRsp getResourceTree(String resourceKey, Boolean isIncludeResource);

    /**
     * 添加根节点
     * @param resourceKey 资源key
     * @param resourceName 资源名称
     * @param resourceValue 资源值
     */
    void addResourceRoot(String resourceKey, String resourceName, String resourceValue);

    /**
     * 获取全部根节点
     * @return
     */
    List<GetResourceRootRsp> getResourceRoot();

    /**
     * 批量添加资源
     * @param resourcePid 资源父id
     * @param resourceType 资源类型
     * @param resourceMap 资源value数组
     */
    void batchAddResource(Long resourcePid, Integer resourceType, Map<String, String> resourceMap);

    /**
     * 添加资源
     * @param resourceKey 资源key
     * @param pResourceValue 资源父value
     * @param resourceType 资源类型
     * @param resourceMap 数据
     */
    void batchAddResourceByKv(String resourceKey, String pResourceValue, Integer resourceType, Map<String, String> resourceMap);

    /**
     * 修改资源
     * @param resourceId 资源id
     * @param resourceName 资源名称
     * @param resourceValue 资源value
     */
    void updateResource(Long resourceId, String resourceName, String resourceValue);

    /**
     * 删除
     * @param resourceId 删除资源
     */
    void delete(Long resourceId);

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

    /**
     * 修改资源
     * @param resourceKey 资源key
     * @param resourceValue 资源value
     * @param resourceName 资源名称
     */
    void updateResourceByKv(String resourceKey, String resourceValue, String resourceName);


    /**
     * 删除
     * @param resourceKey 资源key
     * @param resourceValue 资源value
     */
    void deleteByResourceByKv(String resourceKey, String resourceValue);

    /**
     * 父子移动节点
     * @param resourceKey 资源key
     * @param resourceValue 资源value
     * @param pResourceValue 父资源value
     */
    void fsMoveByKv(String resourceKey, String resourceValue, String pResourceValue);

    /**
     * 兄弟移动节点
     * @param resourceKey 资源key
     * @param resourceValue 资源value
     * @param moveOp 1上移 0下移
     */
    void btMoveByKv(String resourceKey, String resourceValue, Integer moveOp);
}
