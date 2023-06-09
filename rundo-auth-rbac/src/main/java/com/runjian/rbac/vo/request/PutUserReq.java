package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 修改用户请求体
 * @author Miracle
 * @date 2023/6/9 15:38
 */
@Data
public class PutUserReq {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @Min(value = 1, message = "非法用户id")
    private Long userId;

    /**
     * 生效结束时间
     */
    private LocalDateTime expiryEndTime;

    /**
     * 密码
     */
    @Size(min = 1, max = 100, message = "非法密码，长度范围1~100")
    private String password;

    /**
     * 部门id
     */
    private Long sectionId;

    /**
     * 工作名称
     */
    private String workName;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 描述
     */
    private String description;

    /**
     * 角色id数组
     */
    private Set<Long> roleIds;
}
