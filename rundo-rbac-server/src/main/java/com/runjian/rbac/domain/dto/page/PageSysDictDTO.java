package com.runjian.rbac.domain.dto.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName PageSysDictDTO
 * @Description 字典分页
 * @date 2023-01-31 周二 11:08
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class PageSysDictDTO extends Page {

    @ApiModelProperty("字典分组名称")
    private String groupName;

    @ApiModelProperty("字典分组编码")
    private String groupCode;

    @ApiModelProperty("字典项名称")
    private String itemName;

    @ApiModelProperty("字典值")
    private String itemValue;
}
