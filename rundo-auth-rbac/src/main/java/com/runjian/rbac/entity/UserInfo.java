package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 14:42
 */
@Data
public class UserInfo {

    private Long id;

    /**
     * 部门id
     */
    private Long sectionId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 工作名称
     */
    private String workName;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 有效开始时间
     */
    private LocalDateTime expiryStartTime;

    /**
     * 有效结束时间
     */
    private LocalDateTime expiryEndTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 删除状态
     */
    private Integer deleted;

    /**
     * 创建人
     */
    private String createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
