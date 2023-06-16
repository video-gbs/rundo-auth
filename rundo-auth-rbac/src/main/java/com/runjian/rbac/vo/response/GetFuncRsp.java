package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * 功能返回体
 * @author Miracle
 * @date 2023/6/16 10:18
 */
@Data
public class GetFuncRsp {

    /**
     * 数据Id
     */
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
     * 功能名称
     */
    private String funcName;

    /**
     * 范围
     */
    private String scope;

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
}
