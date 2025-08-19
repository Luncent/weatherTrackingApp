package integration;

import annotations.IT;
import org.example.entities.HttpSession;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.LoginService;
import org.example.services.RegistrationService;
import org.example.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static test_constants.UserConstants.ANDREW;


@IT
public class LoginServiceTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private SessionService sessionService;

    @Test
    @DisplayName("user logins existing account and session opens")
    public void existingUserSuccessLoginWithCorrectPasswordAndSessionCreated(@Value("${session_duration_sec}") int sessionTimeSec) throws Exception {
        registrationService.register(ANDREW.getLogin(), ANDREW.getPassword());
        SECONDS.sleep(1);
        UUID sessionId = loginService.login(ANDREW.getLogin(), ANDREW.getPassword());
        SECONDS.sleep(sessionTimeSec - 1);
        HttpSession session =sessionService.findByIdAndCheckActive(sessionId);
        assertTrue(sessionService.isSessionActive(session));
    }

    @Test
    @DisplayName("user logins with wrong password or login")
    public void userGetsExceptionWhenLoginWithWrongPasswordOrLogin() throws Exception {
        registrationService.register(ANDREW.getLogin(), ANDREW.getPassword());
        assertAll(
                ()->assertThrows(EntityNotFoundException.class, ()->loginService.login("wrong", ANDREW.getPassword())),
                ()->assertThrows(EntityNotFoundException.class, ()->loginService.login(ANDREW.getLogin(), "wrong"))
        );
    }

}
