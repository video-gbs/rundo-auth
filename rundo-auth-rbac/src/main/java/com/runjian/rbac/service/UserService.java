package com.runjian.rbac.service;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetUserPageRsp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/31 14:39
 */
public interface UserService {

    /**
     * 根据角色Id查询绑定与未绑定的数据
     */
    PageInfo<GetUserPageRsp> getUserPageByRoleId(int page, int num, Long roleId, String username, Boolean isBinding);

    /**
     * 根据部门层级查询用户
     */
    PageInfo<GetUserPageRsp> getUserPage(int page, int num, Long sectionId, String username, String workName, Boolean isInclude);


    /**
     * 禁用启用用户
     */
    void disabled(String authUser, Long userId, Integer disabled);

    /**
     * 新增用户
     */
    void addUser(String authUser, String username, String password, Long sectionId, LocalDateTime expiryStartTime, LocalDateTime expiryEndTime,
                 String workName, String workNum, String address, String phone, String description,
                 Set<Long> roleIds);

    /**
     * 编辑用户
     */
    void updateUser(String authUser, Long userId, LocalDateTime expiryEndTime, String password, Long sectionId,
                    String workName, String workNum, String phone, String address, String description, Set<Long> roleIds);

    /**
     * 批量删除
     */
    void batchDeleteUser(String authUser, Set<Long> userIds);
}
