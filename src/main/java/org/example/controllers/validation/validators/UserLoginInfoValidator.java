package org.example.controllers.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.example.controllers.validation.annotations.UserLoginInfo;
import org.example.dto.user.UserLoginDTO;
import org.springframework.stereotype.Component;

@Component
public class UserLoginInfoValidator implements ConstraintValidator<UserLoginInfo, UserLoginDTO> {
    @Override
    public boolean isValid(UserLoginDTO loginDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !StringUtils.isBlank(loginDTO.getLogin()) && !StringUtils.isBlank(loginDTO.getPassword());
    }
}
