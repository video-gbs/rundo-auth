package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgVO
 * @Description
 * @date 2023-01-10 周二 10:02
 */
@Data
public class SysOrgVO {

    @ApiModelProperty("部门编号")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;
    @ApiModelProperty("上级部门")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long orgPid;

    @ApiModelProperty("部门名称")
    private String orgName;

    @ApiModelProperty("部门负责人")
    private String orgLeader;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String adders;

    @ApiModelProperty("组织机构排序")
    private Integer orgSort;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("描述信息")
    private String description;
}
