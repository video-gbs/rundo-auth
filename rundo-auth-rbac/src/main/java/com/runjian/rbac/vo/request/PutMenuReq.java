package com.runjian.rbac.vo.request;

import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import com.runjian.rbac.constant.MenuType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Objects;

/**
 * 修改菜单请求体
 * @author Miracle
 * @date 2023/6/12 15:30
 */
@Data
public class PutMenuReq implements ValidatorFunction {

    /**
     * 菜单id
     */
    @NotNull(message = "菜单Id不能为空")
    @Min(value = 1, message = "非法菜单id")
    private Long id;

    /**
     * 菜单排序
     */
    @NotNull(message = "菜单排序不能为空")
    @Min(value = 0, message = "非法菜单排序")
    private Integer menuSort;

    /**
     * 菜单类型 0-抽象 1-目录 2-页面
     */
    @NotNull(message = "菜单类型不能为空")
    @Range(min = 0, max = 2, message = "非法菜单类型状态")
    private Integer menuType;

    /**
     * 跳转路径
     */
    @Size(max = 250, message = "非法菜单图标")
    private String path;

    /**
     * 组件
     */
    @Size(max = 250, message = "非法菜单组件")
    private String component;

    /**
     * 菜单名称
     */
    @Size(max = 100, message = "非法菜单名称")
    private String name;

    /**
     * 菜单图标
     */
    @Size(max = 100, message = "非法菜单图标")
    private String icon;

    /**
     * 描述
     */
    @Size(max = 250, message = "非法描述")
    private String description;

    /**
     * 是否全屏
     */
    @Range(min = 0, max = 1, message = "非法全屏选项")
    private Integer isFullScreen;

    /**
     * 是否禁用
     */
    @NotNull(message = "禁用状态不能为空")
    @Range(min = 0, max = 1, message = "非法禁用状态")
    private Integer disabled;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if(!Objects.equals(this.menuType, MenuType.ABSTRACT.getCode())){
            if (Objects.isNull(this.path)) {
                result.getErrorMsgMap().put("修改失败", "跳转路径不能为空");
                result.setHasErrors(true);

            }
            if (Objects.isNull(this.component)) {
                result.getErrorMsgMap().put("修改失败", "前端组件不能为空");
                result.setHasErrors(true);
            }
        }
    }
}
