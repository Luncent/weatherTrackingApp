package org.example.controllers.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.controllers.validation.validators.UserLoginInfoValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserLoginInfoValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLoginInfo {
    String message() default "cant use whitespaces as login or password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
