package org.example.dto.requests_dtos;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String login;
    private String password;
}
