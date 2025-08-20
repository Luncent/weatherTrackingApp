package org.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exception_handling.exceptions.repository.EntityExistsException;
import org.example.exception_handling.exceptions.service.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.example.enums.AuthErrorType.REGISTER;

@Service
@AllArgsConstructor
@Log4j2
public class RegistrationService {
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public UUID register(String login, String password){
        try {
            User newUser = userService.save(login, BCrypt.hashpw(password, BCrypt.gensalt()));
            HttpSession session = sessionService.openSessionForUser(newUser);
            return session.getId();
        }catch (EntityExistsException e){
            throw new AuthException("User "+login+" already exists", REGISTER);
        }
    }
}
