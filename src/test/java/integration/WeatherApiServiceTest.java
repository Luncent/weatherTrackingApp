package integration;

import annotations.IT;
import lombok.AllArgsConstructor;
import org.example.dto.LocationWeatherDTO;
import org.example.model.Coordinate;
import org.example.services.WeatherAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;

@IT
public class WeatherApiServiceTest {

    private final static String LONGITUDE = "28.3325";
    private final static String LATITUDE = "54.0983";

    @Autowired
    private WeatherAPIService weatherAPIService;

    @Test
    public void successfulRequestHandling() throws Exception {
        Coordinate coordinate = new Coordinate(valueOf(Double.parseDouble(LONGITUDE)),
                valueOf(Double.parseDouble(LATITUDE)));
        LocationWeatherDTO dto = weatherAPIService.getLocationWeatherByCoordinates(
                coordinate
        );
        System.out.println(dto);
    }
}
