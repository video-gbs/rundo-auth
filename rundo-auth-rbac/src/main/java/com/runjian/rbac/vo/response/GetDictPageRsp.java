package com.runjian.rbac.vo.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/6/5 10:15
 */
@Data
public class GetDictPageRsp {

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
