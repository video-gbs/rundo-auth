package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 刷新用户资源缓存请求体
 * @author Miracle
 * @date 2023/7/31 15:32
 */
@Data
public class PutRefreshUserResourceReq {

    @NotBlank(message = "资源key不能为空")
    @Size(min = 1, max = 999999, message = "非法资源Key")
    private String resourceKey;
}
