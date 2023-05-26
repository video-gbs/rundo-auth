package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysOrgDTO
 * @Description
 * @date 2023-01-09 周一 18:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "部门信息", description = "部门信息")
public class SysOrgDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "上级部门", required = true)
    @NotNull(message = "上级部门Id不能为空")
    private Long orgPid;

    @ApiModelProperty(value = "部门名称", required = true)
    @NotNull(message = "部门名称名称不能为空")
    @NotBlank(message = "部门名称名称 或者只包含空格")
    @NotEmpty(message = "部门名称名称不能为空")
    @Length(max = 32 ,message = "部门名称,最长为32字符")
    private String orgName;

    @ApiModelProperty("排序")
    private Integer orgSort;

    @ApiModelProperty(value = "部门负责人")
    @Length(max = 32,message = "部门负责人名称,最长为32字符")
    private String orgLeader;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "地址")
    private String adders;

    @ApiModelProperty(value = "描述信息")
    @Length(max = 128,message = "描述信息，最大长度128个字符")
    private String description;
}
