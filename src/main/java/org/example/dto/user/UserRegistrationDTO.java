package org.example.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.validation.annotations.UserRegistrationInfo;

@Data
@UserRegistrationInfo
public class UserRegistrationDTO {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirm;
}
