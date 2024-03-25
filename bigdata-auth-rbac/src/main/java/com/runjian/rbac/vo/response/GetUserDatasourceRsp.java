package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/20 18:00
 */
@Data
public class GetUserDatasourceRsp {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 主体id
     */
    private Long mainId;

    /**
     * 主体名称
     */
    private String mainName;

    /**
     * 名称
     */
    private Integer readWriteAuth;

    /**
     * 描述
     */
    private LocalDateTime validTime;

}
