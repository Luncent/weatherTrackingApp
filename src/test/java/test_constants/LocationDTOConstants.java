package test_constants;

import org.example.dto.LocationWeatherDTO;
import org.example.dto.UnsavedLocationDTO;

import static java.math.BigDecimal.valueOf;

public class LocationDTOConstants {
    public final static LocationWeatherDTO EXPECTED_WEATHER_DTO_FROM_JSON_FILE = LocationWeatherDTO.builder()
            .temperature(valueOf(21.16))
            .feelsLikeTemperature(valueOf(21.42))
            .humidity(80)
            .longitude(valueOf(28.3325))
            .latitude(valueOf(54.0983))
            .countryCode("BY")
            .weatherDescription("light rain")
            .city("Zhodzina")
            .build();

    public final static UnsavedLocationDTO LOCATION_WEATHER_DTO = new UnsavedLocationDTO(
            "London", valueOf(51.5074219), valueOf(-0.1246474));

    public final static String CITY_NAME = "London";
}
