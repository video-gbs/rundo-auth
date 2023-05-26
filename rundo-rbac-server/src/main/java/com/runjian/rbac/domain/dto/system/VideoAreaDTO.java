package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName VideoAraeDTO
 * @Description 添加安防区域
 * @date 2023-01-13 周五 14:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "VideoArea对象", description = "安保区域")
public class VideoAreaDTO {

    @Schema(title = "areaName", description = "安防区域ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @Schema(name = "areaName", description = "安防区域名称")
    @Length(min = 1, max = 32, message = "内容过大")
    @NotNull(message = "安防区域名称不能为空")
    @NotBlank(message = "安防区域名称 或者只包含空格")
    @NotEmpty(message = "安防区域名称不能为空")
    private String areaName;

    @Schema(name = "areaPid", description = "直接上级")
    @NotNull(message = "上级区域，为必填项")
    private Long areaPid;

    @Schema(name = "description", description = "描述信息")
    @Length(max = 128, message = "描述信息内容过长，支持最大长度128个字符")
    private String description;
}
