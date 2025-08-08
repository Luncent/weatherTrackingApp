package test_constants;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserConstants {

    public static final TestUserDTO ANDREW = new TestUserDTO("Andrev", "123", "123");

    @Data
    @AllArgsConstructor
    public static class TestUserDTO {
        public String login;
        public String password;
        public String repeatedPassword;
    }
}
