package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/18 17:55
 */
@Data
public class GetEngineRsp {

    /**
     * 引擎名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建人 名称
     */
    private String createByName;

    /**
     * 创建人 id
     */
    private Long createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人 id
     */
    private Long updateById;

    /**
     * 修改人 名称
     */
    private String updateByName;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
