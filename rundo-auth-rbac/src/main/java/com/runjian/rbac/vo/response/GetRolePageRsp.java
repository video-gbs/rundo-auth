package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色分页返回体
 * @author Miracle
 * @date 2023/6/2 9:46
 */
@Data
public class GetRolePageRsp {


    private Long id;

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


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
