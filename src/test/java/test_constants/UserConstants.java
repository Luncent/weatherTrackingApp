package test_constants;

import integration.RegistrationServiceTest;
import lombok.AllArgsConstructor;
import lombok.Data;

public class UserConstants {

    public static final TestUserDTO ANDREW = new TestUserDTO("Andrew", "123", "123");

    @Data
    @AllArgsConstructor
    public static class TestUserDTO {
        public String login;
        public String password;
        public String repeatedPassword;
    }
}
