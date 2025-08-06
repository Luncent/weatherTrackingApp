package org.example.controllers.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.controllers.validation.validators.UserRegistrationInfoValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserRegistrationInfoValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRegistrationInfo {
    String message() default "Passwords dont match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
