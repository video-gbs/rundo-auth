package com.runjian.rbac.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 获取角色分页数据，已绑定数据置顶
 * @author Miracle
 * @date 2023/7/14 11:19
 */
@Data
public class GetUserRolePageRsp {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 创建人
     */
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
