package utils;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonTestUtils {

    public static String getJsonFromFile(String path) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new ClassPathResource(path).getInputStream())) {
            return new String(bis.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
