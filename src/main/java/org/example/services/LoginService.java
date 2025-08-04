package org.example.services;

import lombok.AllArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LoginService {

    private final PasswordEncodingService passwordEncodingService;
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public UUID login(String login, String password) throws EntityNotFoundException {
        User user = userService.findByLogin(login);
        if(!passwordEncodingService.isSamePassword(password, user.getPassword())) {
            throw new EntityNotFoundException();
        }
        HttpSession session = sessionService.openSessionForUser(user);
        return session.getId();
    }
}
