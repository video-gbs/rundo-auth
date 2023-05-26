package com.runjian.rbac.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MyMetaClass {
    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("菜单名称")
    private String title;

    public MyMetaClass(String title, String icon) {
        this.icon = icon;
        this.title = title;
    }
}