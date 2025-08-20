package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exception_handling.exceptions.service.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.example.enums.AuthErrorType.LOGIN;

@Service
@AllArgsConstructor
@Log4j2
public class LoginService {

    private static final String USER_LOGIN_ERROR_MSG = "user name or password is incorrect";

    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public UUID login(String login, String password){
        try {
            User user = userService.findByLogin(login);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new AuthException(USER_LOGIN_ERROR_MSG, LOGIN);
            }
            HttpSession session = sessionService.openSessionForUser(user);
            return session.getId();
        }catch (EntityNotFoundException e){
            throw new AuthException(USER_LOGIN_ERROR_MSG, LOGIN);
        }
    }
}
