package com.runjian.common.validator.constraints.annotation;

import com.runjian.common.validator.constraints.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {

    String message() default "{ValidEmail.userDto}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
