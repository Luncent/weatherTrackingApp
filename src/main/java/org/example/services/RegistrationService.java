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
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class RegistrationService {

    private static String PASSWORDS_MATCH_ERROR = "Passwords do not match";

    private final UserService userService;
    private final SessionService sessionService;
    private final PasswordEncodingService passwordEncodingService;

    //TODO add EntityExistsException to transaction rollback classes mb it will handle
    //TODO it correctly an wont cause UnexpectedRollbackException 
    @Transactional
    public UUID register(String login, String password, String passwordRepeated) throws EntityExistsException{
        User newUser = null;
        try {
            newUser = userService.save(login, passwordEncodingService.encryptPassword(password));
            log.debug(newUser.getPassword());
        }
        catch (ConstraintViolationException e) {
            log.error("user with login exists: "+ e.getMessage());
            throw new EntityExistsException("user with login exists");
        }
        HttpSession session = sessionService.openSessionForUser(newUser);
        return session.getId();
    }
}
