package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 15:03
 */
@Data
public class DictInfo {

    private Long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 字典名称
     */
    private String itemName;

    /**
     * 字典值
     */
    private String itemValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
