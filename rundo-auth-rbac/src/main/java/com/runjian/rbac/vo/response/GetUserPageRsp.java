package com.runjian.rbac.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户分页返回体
 * @author Miracle
 * @date 2023/6/1 11:38
 */
@Data
public class GetUserPageRsp {

    /**
     * 主键id
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryStartTime;

    /**
     * 有效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 创建人
     */
    private String createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
