package com.runjian.rbac.vo.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * @author Miracle
 * @date 2023/7/18 19:31
 */
@Data
public class PutResourceBtMoveKvReq {

    /**
     * 资源key
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceKey;

    /**
     * 资源value
     */
    @NotBlank(message = "资源key不能为空")
    private String resourceValue;

    /**
     * 移动指令，0-下移 1-上移
     */
    @NotNull(message = "移动指令不能为空")
    @Range(min = 0, max = 1, message = "非法移动指令")
    private Integer moveOp;
}
