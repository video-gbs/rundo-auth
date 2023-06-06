package com.runjian.rbac.vo.response;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/6/6 14:25
 */
@Data
public class GetFuncResourceRsp {

    private Long id;

    /**
     * 功能id
     */
    private Long funcId;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 是否校验
     */
    private Integer validated;

    /**
     * 校验参数
     */
    private String validateParam;

    /**
     * 是否禁用
     */
    private Integer disabled;
}
