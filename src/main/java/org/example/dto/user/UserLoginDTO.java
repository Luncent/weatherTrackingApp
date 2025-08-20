package org.example.dto.user;

import lombok.Data;
import org.example.validation.annotations.UserLoginInfo;

@Data
@UserLoginInfo
public class UserLoginDTO {
    private String login;
    private String password;
}
