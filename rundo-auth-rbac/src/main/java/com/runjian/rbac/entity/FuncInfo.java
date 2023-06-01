package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/31 10:24
 */
@Data
public class FuncInfo {

    private Long id;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务前缀
     */
    private String prefixPath;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 方法
     */
    private Integer method;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private String createBy;

    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
