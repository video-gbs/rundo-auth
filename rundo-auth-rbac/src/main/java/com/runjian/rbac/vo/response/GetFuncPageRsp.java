package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * 功能分页返回体
 * @author Miracle
 * @date 2023/6/6 9:58
 */
@Data
public class GetFuncPageRsp {

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

}
