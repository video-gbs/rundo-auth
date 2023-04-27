package com.runjian.common.validator.constraints.annotation;

import com.runjian.common.validator.constraints.PwdValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Miracle
 * @date 2023/4/12 10:14
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PwdValidator.class})
@Documented
public @interface ValidPassword {

    String[] except() default {};

    String message() default "密码不符合规范";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
