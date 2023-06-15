package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户基础信息返回体
 * @author Miracle
 * @date 2023/6/6 16:33
 */
@Data
public class GetUserRsp {

    /**
     * 用户名
     */
    private String username;

    /**
     * 工作名称
     */
    private String workName;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 手机
     */
    private String phone;

    /**
     * 部门名称
     */
    private String sectionName;

    /**
     * 角色名称
     */
    private Set<String> roleNames;

    /**
     * 账号有效截止时间
     */
    private LocalDateTime expiryEndTime;
}
