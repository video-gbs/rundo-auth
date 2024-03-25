package com.runjian.common.validator.constraints;

import com.runjian.common.validator.constraints.annotation.ValidPassword;
import org.passay.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 密码校验工具
 * @author Miracle
 * @date 2023/4/12 10:13
 */
public class PwdValidator implements ConstraintValidator<ValidPassword, String> {


    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        var validator = new PasswordValidator(Arrays.asList(
                // 长度在8-30位
                new LengthRule(8,30),
                // 至少拥有一个大写字母
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                // 至少拥有一个小写字母
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                // 至少有一个数字
                new CharacterRule(EnglishCharacterData.Digit, 1),
                // 至少拥有一个特殊字符
                new CharacterRule(EnglishCharacterData.Special, 1),
                // 不允许连续的5个字母
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
                // 不允许连续的5个数字
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
                // 不允许键盘上连续的5个值
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
                // 不允许包含空格
                new WhitespaceRule()
        ));
        var result = validator.validate(new PasswordData(password));
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(String.join(",", validator.getMessages(result))).addConstraintViolation();
        return result.isValid();
    }

}
