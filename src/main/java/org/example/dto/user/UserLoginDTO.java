package org.example.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginDTO {
    @NotBlank(message = "Логин не должен быть пустым")
    @Length(min = 5, max = 20, message = "длина логина должна находится в диапазоне от 5 до 20 символов")
    private String login;
    @NotBlank(message = "Пароль не должен быть пустым")
    @Length(min = 5, max = 20, message = "длина пароля должна находится в диапазоне от 5 до 20 символов")
    private String password;
}
