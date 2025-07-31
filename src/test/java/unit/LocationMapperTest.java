package unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LocationWeatherDTO;
import org.example.dto.UnsavedLocationDTO;
import org.example.mappers.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static test_constants.LocationDTOConstants.EXPECTED_WEATHER_DTO_FROM_JSON_FILE;
import static test_constants.LocationDTOConstants.LOCATION_WEATHER_DTO;
import static utils.JsonTestUtils.getJsonFromFile;

@Tag("unit")
public class LocationMapperTest {

    private ObjectMapper objectMapper;
    private LocationMapper locationMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        locationMapper = new LocationMapper(objectMapper);
    }

    @Test
    @DisplayName("Location weather JSON data convertion to obj")
    public void jsonResponseConversionToSavedDTOCorrect() throws IOException {
        String jsonLocationWeather = getJsonFromFile("location_weather_data.json");
        LocationWeatherDTO concerted = locationMapper.convertToLocationWeatherDTO(jsonLocationWeather);
        assertThat(concerted).isEqualTo(EXPECTED_WEATHER_DTO_FROM_JSON_FILE);
    }

    @Test
    @DisplayName("Location JSON data convertion to obj")
    public void jsonResponseConversionToUnsavedDTOCorrect() throws IOException {
        String jsonLocations = getJsonFromFile("searchLocationsByCityNameTestData.json");
        List<UnsavedLocationDTO> weatherList = locationMapper.convertToLocationDTOList(jsonLocations);
        assertThat(LOCATION_WEATHER_DTO).isIn(weatherList);
    }
}
