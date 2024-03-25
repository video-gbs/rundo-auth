package com.runjian.rbac.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 生效开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryStartTime;

    /**
     * 生效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryEndTime;

    /**
     * 密码
     */
    @Size(min = 6, max = 100, message = "非法密码，长度范围6~100")
    private String password;

    /**
     * 部门id
     */
    @Min(value = 0, message = "非法部门id")
    private Long sectionId;

    /**
     * 工作名称
     */
    @Size(min = 1, max = 32, message = "非法名称，长度范围1~32")
    private String workName;

    /**
     * 工号
     */
    @Size(max = 32, message = "非法工号，长度范围1~32")
    private String workNum;

    /**
     * 手机号码
     */
    @Size(max = 20, message = "非法手机号码，长度范围1~20")
    private String phone;

    /**
     * 地址
     */
    @Size(max = 250, message = "非法地址，长度范围1~250")
    private String address;

    /**
     * 描述
     */
    @Size(max = 250, message = "非法地址，长度范围1~250")
    private String description;

    /**
     * 角色id数组
     */
    @Size(max = 9999, message = "非法角色id数组，长度范围0~9999")
    private Set<Long> roleIds;
}
