package integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.example.config.SpringMVCConfig;
import org.example.config.TestConfig;
import org.example.dto.UserDTO;
import org.example.entities.User;
import org.example.exceptions.EntityExistsException;
import org.example.exceptions.ValidationException;
import org.example.services.PasswordEncodingService;
import org.example.services.SessionService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.example.services.RegistrationService;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static test_constants.UserConstants.ANDREW;

/*@ExtendWith({
        SpringExtension.class,
})
@ContextConfiguration(classes = TestConfig.class)*/
@SpringJUnitConfig(classes = TestConfig.class)
@TestPropertySource(properties = {"spring.profiles.active=test"})
@Transactional
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
        UserDTO user = registrationService.register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        SECONDS.sleep(sessionTimeSec-1);

        assertThat(sessionService.isSessionActive(user.sessionId().get())).isTrue();
    }

    @Test
    public void registrationWithExistingLogin() throws Exception {
        registrationService.register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword());
        assertThrows(EntityExistsException.class, () -> registrationService
                .register(ANDREW.getLogin(), ANDREW.getPassword(), ANDREW.getRepeatedPassword()));
    }
}
