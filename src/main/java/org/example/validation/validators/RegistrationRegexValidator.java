package org.example.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.user.UserRegistrationDTO;
import org.example.validation.annotations.RegexConstraint;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationRegexValidator implements ConstraintValidator<RegexConstraint, UserRegistrationDTO> {

    private final Pattern loginPattern;
    private final Pattern passwordPattern;
    public RegistrationRegexValidator() {
        loginPattern = Pattern.compile("[a-zA-Zа-яА-ЯёЁ0-9_]{5,20}");
        passwordPattern = Pattern.compile("[0-9]{5,20}");
    }

    @Override
    public boolean isValid(UserRegistrationDTO registrationDTO, ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        Matcher matcher = loginPattern.matcher(registrationDTO.getLogin());
        boolean loginMatches = matcher.matches();
        if (!loginMatches) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Допустимые символы для логина [a-zA-Zа-яА-ЯёЁ0-9_]").addConstraintViolation();
        }
        matcher = passwordPattern.matcher(registrationDTO.getPassword());
        boolean passwordMatches = matcher.matches();
        if (!passwordMatches) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("Допустимые символы для пароля [0-9]").addConstraintViolation();
        }
        return loginMatches && passwordMatches;
    }
}
