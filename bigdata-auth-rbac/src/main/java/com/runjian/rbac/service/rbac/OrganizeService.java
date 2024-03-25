package com.runjian.rbac.service.rbac;

import com.runjian.rbac.vo.response.GetOrganizeRsp;

import java.util.List;

/**
 * @author Miracle
 * @date 2024/3/18 15:01
 */
public interface OrganizeService {

    /**
     * 分页获取组织
     * @param pageNum 页码
     * @param pageSize 每页数据量
     * @param name 组织名称
     * @param state 组织状态
     * @return
     */
    List<GetOrganizeRsp> getOrganizePage(int pageNum, int pageSize, String name, String state);

    /**
     * 新增组织
     * @param name 组织名称
     * @param description 组织描述
     */
    void addOrganize(String name, String description);

    /**
     * 修改组织
     * @param id 组织id
     * @param name 组织名称
     * @param description 组织描述
     */
    void updateOrganize(Long id, String name, String description);

    /**
     * 删除组织
     * @param id 组织id
     */
    void deleteOrganize(Long id);

    /**
     * 关联用户
     * @param userId 用户id
     */
    void associateUser(Long userId);
}
