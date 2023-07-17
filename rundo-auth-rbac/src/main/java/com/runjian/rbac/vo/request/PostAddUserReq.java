package com.runjian.rbac.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 新增用户请求体
 * @author Miracle
 * @date 2023/6/9 15:11
 */
@Data
public class PostAddUserReq implements ValidatorFunction {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 100, message = "非法用户名，长度范围1~100")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 1, max = 100, message = "非法密码，长度范围6~100")
    private String password;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空")
    @Min(value = 0, message = "非法部门id")
    private Long sectionId;

    /**
     * 生效开始时间
     */
    @NotNull(message = "生效开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryStartTime;

    /**
     * 生效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryEndTime;

    /**
     * 工作名称
     */
    @Size(max = 32, message = "非法名称，长度范围1~32")
    private String workName;

    /**
     * 工号
     */
    @Size(max = 32, message = "非法工号，长度范围1~32")
    private String workNum;

    /**
     * 地址
     */
    @Size(max = 250, message = "非法地址，长度范围1~250")
    private String address;

    /**
     * 手机号码
     */
    @Size(max = 20, message = "非法手机号码，长度范围1~20")
    private String phone;

    /**
     * 描述
     */
    @Size(max = 250, message = "非法描述，长度范围1~250")
    private String description;

    /**
     * 角色id数组
     */
    private Set<Long> roleIds;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (Objects.nonNull(expiryEndTime)){
            if (expiryStartTime.isAfter(expiryEndTime)){
                result.setHasErrors(true);
                result.setErrorMsgMap(Map.of("无效的时间", "生效开始时间在生效结束时间之后"));
            }
        }
    }
}
