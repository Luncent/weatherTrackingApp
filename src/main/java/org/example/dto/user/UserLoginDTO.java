package org.example.dto.user;

import lombok.Data;
import org.example.controllers.validation.annotations.UserLoginInfo;

@Data
@UserLoginInfo
public class UserLoginDTO {
    private String login;
    private String password;
}
