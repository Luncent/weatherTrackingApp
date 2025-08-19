package org.example.controllers.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.controllers.validation.annotations.UserRegistrationInfo;
import org.example.dto.user.UserRegistrationDTO;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class UserRegistrationInfoValidator implements ConstraintValidator<UserRegistrationInfo, UserRegistrationDTO> {

    @Override
    public boolean isValid(UserRegistrationDTO userRegistrationDTO, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.equals(userRegistrationDTO.getPassword(), userRegistrationDTO.getPasswordConfirm());
    }
}
