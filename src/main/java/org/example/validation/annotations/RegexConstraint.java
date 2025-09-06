package org.example.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validation.validators.RegistrationRegexValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RegistrationRegexValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegexConstraint {
    String message() default "Разрешено использовать только цифры и буквы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
