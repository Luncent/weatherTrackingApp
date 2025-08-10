package test_constants;

import org.example.dto.LocationWeatherDTO;
import org.example.dto.UnsavedLocationDTO;

import static java.math.BigDecimal.valueOf;

public class LocationDTOConstants {
    public final static LocationWeatherDTO EXPECTED_WEATHER_DTO_FROM_JSON_FILE =  new LocationWeatherDTO(
            valueOf(21.16), valueOf(21.42), 80, valueOf(28.3325), valueOf(54.0983), "BY",
            "light rain", "Zhodzina"
    );

    public final static UnsavedLocationDTO LOCATION_WEATHER_DTO = new UnsavedLocationDTO(
            "London", valueOf(51.5074219), valueOf(-0.1246474));

    public final static String CITY_NAME = "London";
}
