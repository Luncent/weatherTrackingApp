package org.example.dto.requests_dtos;

import lombok.Data;
import lombok.Value;

@Data
public class UserRegistrationDTO {
    private String login;
    private String password;
    private String passwordConfirm;
}
