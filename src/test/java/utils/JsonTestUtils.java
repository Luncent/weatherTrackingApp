package utils;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class JsonTestUtils {



    /*public static Stream<Arguments> getArgumentsForMethod() throws IOException {
        return Stream.of(Arguments.of(getJsonFromFile("location_weather_data.json")));
    }*/

    public static String getJsonFromFile(String path) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new ClassPathResource(path).getInputStream())) {
            return new String(bis.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
