package com.runjian.common.validator;

import com.runjian.common.config.exception.BusinessException;

/**
 * @author Miracle
 * @date 2022/4/21 9:12
 */
public interface ValidatorFunction {

    void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException;
}
