package org.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.dto.UserDTO;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.ValidationException;
import org.example.mappers.UserMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class RegistrationService {

    private static String PASSWORDS_MATCH_ERROR = "Passwords do not match";

    private final UserService userService;
    private final SessionService sessionService;
    private final PasswordEncodingService passwordEncodingService;
    private final UserMapper userMapper;

    @Transactional
    public UserDTO register(String login, String password, String passwordRepeated) throws EntityExistsException, ValidationException, Exception {
        if (!Objects.equals(password, passwordRepeated)) {
            log.error(PASSWORDS_MATCH_ERROR);
            throw new ValidationException(PASSWORDS_MATCH_ERROR);
        }
        User newUser = null;
        try {
            newUser = userService.save(login, passwordEncodingService.encryptPassword(password));
            log.debug(newUser.getPassword());
        }
        catch (ConstraintViolationException e) {
            log.error("user with login exists: "+ e.getMessage());
            throw new EntityExistsException("user with login exists");
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Some error with db");
        }
        HttpSession session = sessionService.openSessionForUser(newUser);
        return userMapper.userToUserDTO(newUser, session);
    }
}
