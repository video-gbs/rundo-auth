package com.runjian.rbac.service.rbac;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetResourcePageRsp;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/6/6 10:52
 */
public interface ResourceService {

    /**
     * 分页查询
     * @param page 页数
     * @param num 数量
     * @param name 资源名字
     * @param resourceKey 资源组
     * @return
     */
    PageInfo<GetResourcePageRsp> getResourcePage(int page, int num, String name, String resourceKey);

    /**
     * 批量添加资源
     * @param groupName 资源组名称
     * @param resourceName 资源名称
     * @param resourceKey 资源Key
     * @param resourceValue 资源value
     */
    void batchAddResource(String groupName, String resourceName, String resourceKey, Set<String> resourceValue);

    /**
     * 修改资源
     * @param resourceId 资源id
     * @param groupName 资源组名称
     * @param resourceName 资源名称
     * @param resourceKey 资源Key
     * @param resourceValue 资源value
     */
    void updateResource(Long resourceId, String groupName, String resourceName, String resourceKey, String resourceValue);

    /**
     * 批量删除
     * @param resourceIds 资源id组
     */
    void batchDelete(Set<Long> resourceIds);

}
