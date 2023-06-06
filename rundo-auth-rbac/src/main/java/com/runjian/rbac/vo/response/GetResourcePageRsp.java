package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/6/6 11:03
 */
@Data
public class GetResourcePageRsp {

    private Long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源key
     */
    private String resourceKey;

    /**
     * 资源value
     */
    private String resourceValue;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
