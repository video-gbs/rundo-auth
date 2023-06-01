package com.runjian.rbac.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Miracle
 * @date 2023/5/30 14:59
 */
@Data
public class SectionInfo {

    private Long id;

    /**
     * 部门父id
     */
    private Long sectionPid;

    /**
     * 部门名称
     */
    private String sectionName;

    /**
     * 部门排序
     */
    private Long sectionSort;

    /**
     * 负责人名称
     */
    private String leaderName;

    /**
     * 电话
     */
    private String phone;

    /**
     * 层级
     */
    private String level;

    /**
     * 描述
     */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
