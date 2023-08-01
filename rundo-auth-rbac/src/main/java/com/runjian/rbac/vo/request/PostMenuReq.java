package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 添加菜单请求体
 * @author Miracle
 * @date 2023/6/9 17:39
 */
@Data
public class PostMenuReq {

    /**
     * 菜单父id
     */
    @NotNull(message = "菜单父id不能为空")
    @Min(value = 0, message = "菜单id父不能为空")
    private Long menuPid;

    /**
     * 菜单排序
     */
    @NotNull(message = "菜单排序不能为空")
    @Min(value = 1, message = "菜单排序不能为空")
    private Integer menuSort;

    /**
     * 菜单类型
     */
    @NotNull(message = "非法菜单类型不能为空")
    @Range(min = 0, max = 2, message = "非法菜单类型")
    private Integer menuType;

    /**
     * 跳转路径
     */
    @NotBlank(message = "跳转路径不能为空")
    @Size(min = 1, max = 250, message = "前端组件长度范围1~250")
    private String path;

    /**
     * 前端组件
     */
    @NotBlank(message = "前端组件不能为空")
    @Size(min = 1, max = 250, message = "前端组件长度范围1~250")
    private String component;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 1, max = 50, message = "菜单名称长度范围1~50")
    private String name;

    /**
     * 菜单图标
     */
    @Size(max = 250, message = "菜单图标长度范围1~250")
    private String icon;

    /**
     * 描述
     */
    @Size(max = 250, message = "描述长度范围1~250")
    private String description;

    /**
     * 是否全屏
     */
    @Range(min = 0, max = 1, message = "非法全屏选项")
    private Integer isFullScreen;

    /**
     * 是否禁用
     */
    @NotNull(message = "禁用类型不能为空")
    @Range(min = 0, max = 1, message = "非法禁用类型")
    private Integer disabled;
}
