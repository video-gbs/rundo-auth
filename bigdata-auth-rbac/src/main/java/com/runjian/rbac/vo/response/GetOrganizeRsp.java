package com.runjian.rbac.vo.response;

import java.time.LocalDateTime;

/**
 * 获取组织信息返回体
 * @author Miracle
 * @date 2024/3/18 15:16
 */
public class GetOrganizeRsp {

    /**
     * 组织名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否禁用
     */
    private Integer disabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
