package com.runjian.rbac.entity.relation;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/14 16:12
 */
@Data
public class DatasourceAuthRel {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 主体id：个人或者组织
     */
    private Long mainId;

    /**
     * 资源id
     */
    private Long datasourceId;

    /**
     * 主体类型：个人或者组织
     */
    private Integer mainType;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 读写权限
     */
    private Integer readWriteAuth;

    /**
     * 过期时间
     */
    private LocalDateTime validityTime;

    /**
     * 授权人
     */
    private Long authBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
