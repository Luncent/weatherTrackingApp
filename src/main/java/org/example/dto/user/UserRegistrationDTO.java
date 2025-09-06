package org.example.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.validation.annotations.RegexConstraint;
import org.example.validation.annotations.PasswordsMatchConstraint;
import org.hibernate.validator.constraints.Length;

@Data
@PasswordsMatchConstraint
@RegexConstraint
public class UserRegistrationDTO {
    @NotBlank(message = "Логин не должен быть пустым")
    @Length(min = 5, max = 20, message = "Длина логина должна находится в диапазоне от 5 до 20 символов")
    private String login;
    @NotBlank(message = "Пароль не должен быть пустым")
    @Length(min = 5, max = 20, message = "Длина пароля должна находится в диапазоне от 5 до 20 символов")
    private String password;
    @NotBlank(message = "Подтверждение пароля не должно быть пустым")
    @Length(min = 5, max = 20, message = "Длина повтора пароля должна находится в диапазоне от 5 до 20 символов")
    private String passwordConfirm;
}
