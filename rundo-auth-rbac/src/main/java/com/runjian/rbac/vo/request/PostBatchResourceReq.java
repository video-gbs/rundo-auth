package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Map;
import java.util.Set;

/**
 * 批量添加资源请求体
 * @author Miracle
 * @date 2023/6/9 17:05
 */
@Data
public class PostBatchResourceReq {

    /**
     * 父节点id
     */
    @NotNull(message = "父节点id不能为空")
    @Min(value = 0, message = "非法父节点")
    private Long resourcePid;

    /**
     * 资源类型 1-目录 2-资源
     */
    @NotNull(message = "资源类型不能为空")
    @Range(min = 1, max = 2, message = "非法资源类型")
    private Integer resourceType;

    /**
     * 资源Map resourceValue:resourceName
     */
    @NotNull(message = "资源value不能为空")
    @Size(min = 1, message = "资源value不能为空")
    private Map<String,String> resourceMap;
}
