package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2024/3/21 15:01
 */
@Data
public class GetTeamRsp {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 责任人id
     */
    private Long principalId;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
