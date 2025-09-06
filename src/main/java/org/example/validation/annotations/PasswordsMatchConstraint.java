package org.example.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validation.validators.RegistrationPasswordsValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RegistrationPasswordsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsMatchConstraint {
    String message() default "Passwords dont match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
