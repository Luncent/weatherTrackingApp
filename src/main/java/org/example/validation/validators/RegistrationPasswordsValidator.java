package org.example.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.validation.annotations.PasswordsMatchConstraint;
import org.example.dto.user.UserRegistrationDTO;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
public class RegistrationPasswordsValidator implements ConstraintValidator<PasswordsMatchConstraint, UserRegistrationDTO> {

    @Override
    public boolean isValid(UserRegistrationDTO userRegistrationDTO, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.equals(userRegistrationDTO.getPassword(), userRegistrationDTO.getPasswordConfirm());
    }
}
