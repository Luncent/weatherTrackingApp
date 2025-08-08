package integration;

import annotations.IT;
import org.example.entities.HttpSession;
import org.example.exceptions.EntityExistsException;
import org.example.services.PasswordEncodingService;
import org.example.services.RegistrationService;
import org.example.services.SessionService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test_constants.UserConstants.ANDREW;


@IT
public class RegistrationServiceTest {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private PasswordEncodingService passwordEncodingService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;


    @Test
    public void registrationOfNonExistingUserWithSessionCreation(@Value("${session_duration_sec}") int sessionTimeSec) throws Exception {
        //register new user
        UUID sessionId = registrationService.register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        SECONDS.sleep(sessionTimeSec-1);
        HttpSession session = sessionService.findById(sessionId);
        assertTrue(sessionService.isSessionActive(session));
    }

    @Test
    public void registrationWithExistingLogin() throws Exception {
        registrationService.register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        assertThrows(EntityExistsException.class, () -> registrationService
                .register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword()));
    }
}
