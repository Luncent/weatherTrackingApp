package org.example.dto.requests_dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;
import org.example.controllers.validation.annotations.UserRegistrationInfo;

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
