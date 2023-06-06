package com.runjian.rbac.service;

import com.github.pagehelper.PageInfo;
import com.runjian.rbac.vo.response.GetUserPageRsp;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Miracle
 * @date 2023/5/31 14:39
 */
public interface UserService {

    /**
     * 根据角色Id查询绑定与未绑定的数据
     * @param page 页码
     * @param num 数量
     * @param roleId 角色id
     * @param username 用户名
     * @param isBinding 是否绑定的数据
     * @return GetUserPageRsp
     */
    PageInfo<GetUserPageRsp> getUserPage(int page, int num, Long roleId, String username, Boolean isBinding);

    /**
     * 根据部门层级查询用户
     * @param page 页码
     * @param num 数量
     * @param sectionId 部门id
     * @param username 用户名
     * @param workName 工作名称
     * @param isInclude 是否包含子节点数据
     * @return GetUserPageRsp
     */
    PageInfo<GetUserPageRsp> getUserPage(int page, int num, Long sectionId, String username, String workName, Boolean isInclude);


    /**
     * 禁用启用用户
     * @param authUser 授权用户
     * @param userId 用户id
     * @param disabled 是否禁用
     */
    void disabled(String authUser, Long userId, Integer disabled);

    /**
     * 新增用户
     * @param authUser 授权用户
     * @param username 用户名
     * @param password 密码
     * @param sectionId 部门id
     * @param expiryStartTime 生效开始时间
     * @param expiryEndTime 生效结束时间
     * @param workName 工作名称
     * @param workNum 工号
     * @param address 地址
     * @param phone 手机号码
     * @param description 描述
     * @param roleIds 角色id数组
     */
    void addUser(String authUser, String username, String password, Long sectionId, LocalDateTime expiryStartTime, LocalDateTime expiryEndTime,
                 String workName, String workNum, String address, String phone, String description,
                 Set<Long> roleIds);

    /**
     * 编辑用户
     * @param authUser 授权用户
     * @param userId 用户id
     * @param expiryEndTime 生效结束时间
     * @param password 密码
     * @param sectionId 部门id
     * @param workName 工作名称
     * @param workNum 工号
     * @param phone 手机号码
     * @param address 地址
     * @param description 描述
     * @param roleIds 角色id数组
     */
    void updateUser(String authUser, Long userId, LocalDateTime expiryEndTime, String password, Long sectionId,
                    String workName, String workNum, String phone, String address, String description, Set<Long> roleIds);

    /**
     * 批量删除
     * @param authUser 授权用户
     * @param userIds 用户id数组
     */
    void batchDeleteUser(String authUser, Set<Long> userIds);
}
