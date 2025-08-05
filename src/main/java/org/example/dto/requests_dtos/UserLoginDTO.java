package org.example.dto.requests_dtos;

import lombok.Data;
import org.example.controllers.validation.interfaces.UserLoginInfo;

@Data
@UserLoginInfo
public class UserLoginDTO {
    private String login;
    private String password;
}
