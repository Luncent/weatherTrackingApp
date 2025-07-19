package integration;

import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.example.config.TestConfig;
import org.example.dto.UserDTO;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.LoginService;
import org.example.services.RegistrationService;
import org.example.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static test_constants.UserConstants.ANDREW;

/*@ExtendWith({
        SpringExtension.class,
})
@ContextConfiguration(classes = TestConfig.class)*/
@SpringJUnitConfig(classes = TestConfig.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
@Transactional
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
        UserDTO newUser = registrationService.register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        SECONDS.sleep(1);
        UserDTO user = loginService.login(ANDREW.getLogin(), ANDREW.getPassword());
        SECONDS.sleep(sessionTimeSec - 1);
        assertThat(sessionService.isSessionActive(user.sessionId().get())).isTrue();
    }

    @Test
    @DisplayName("user logins with wrong password or login")
    public void userGetsExceptionWhenLoginWithWrongPasswordOrLogin() throws Exception {
        UserDTO newUser = registrationService
                .register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        assertAll(
                ()->assertThrows(EntityNotFoundException.class, ()->loginService.login(anyString(), ANDREW.getPassword())),
                ()->assertThrows(EntityNotFoundException.class, ()->loginService.login(ANDREW.getLogin(), anyString()))
        );
    }

}
