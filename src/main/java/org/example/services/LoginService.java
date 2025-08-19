package org.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exceptions.EntityNotFoundException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class LoginService {

    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public UUID login(String login, String password) throws EntityNotFoundException {
        User user = userService.findByLogin(login);
        if(!BCrypt.checkpw(password, user.getPassword())) {
            throw new EntityNotFoundException("user name or password is incorrect");
        }
        HttpSession session = sessionService.openSessionForUser(user);
        return session.getId();
    }
}
