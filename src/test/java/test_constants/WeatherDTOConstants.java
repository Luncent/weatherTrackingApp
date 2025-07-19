package test_constants;

import org.example.dto.WeatherDTO;

import static java.math.BigDecimal.valueOf;

public class WeatherDTOConstants {
    public final static WeatherDTO EXPECTED_WEATHER_DTO_FROM_JSON_FILE = WeatherDTO.builder()
            .temperature(valueOf(21.16))
            .feelsLikeTemperature(valueOf(21.42))
            .humidity(80)
            .longitude(valueOf(28.3325))
            .latitude(valueOf(54.0983))
            .countryCode("BY")
            .weatherDescription("light rain")
            .city("Zhodzina")
            .build();
}
