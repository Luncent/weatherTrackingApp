package unit;

import org.example.services.PasswordEncodingService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordEncodingServiceTest {
    @Test
    public void test() {
        PasswordEncodingService service = new PasswordEncodingService();
        String encryptedPassword = service.encryptPassword("Andrew");
        System.out.println(encryptedPassword);

        assertThat(service.isSamePassword("Andrew", encryptedPassword)).isTrue();
    }
}
