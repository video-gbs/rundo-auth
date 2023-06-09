package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 增加字典请求体
 * @author Miracle
 * @date 2023/6/9 16:45
 */
@Data
public class PostAddDictReq {

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    @Size(min = 1, max = 50, message = "分组名称长度范围1~50")
    private String groupName;

    /**
     * 分组编码
     */
    @NotBlank(message = "分组编码不能为空")
    @Size(min = 1, max = 100, message = "分组编码长度范围1~50")
    private String groupCode;

    /**
     * 字典名称
     */
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 1, max = 50, message = "字典名称长度范围1~50")
    private String itemName;

    /**
     * 字典值
     */
    @NotBlank(message = "字典值不能为空")
    @Size(min = 1, max = 100, message = "字典值长度范围1~100")
    private String itemValue;

    /**
     * 描述
     */
    @Size(max = 250, message = "描述范围在1~50")
    private String description;

}
