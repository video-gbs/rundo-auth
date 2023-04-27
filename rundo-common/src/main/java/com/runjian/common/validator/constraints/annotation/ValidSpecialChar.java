package com.runjian.common.validator.constraints.annotation;

import com.runjian.common.validator.constraints.PwdValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 过滤特殊字符
 *
 * @author Miracle
 * @date 2022/6/6 9:33
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PwdValidator.class})
@Documented
public @interface ValidSpecialChar {

    String[] except() default {};

    String message() default "This String have special char";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
