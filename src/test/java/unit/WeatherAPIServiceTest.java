package unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.WeatherDTO;
import org.example.services.WeatherAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static test_constants.WeatherDTOConstants.EXPECTED_WEATHER_DTO_FROM_JSON_FILE;

@Tag("unit")
public class WeatherAPIServiceTest {

    private ObjectMapper objectMapper;
    private WeatherAPIService weatherAPIService;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        weatherAPIService = new WeatherAPIService(objectMapper, any(), any());
    }

    @ParameterizedTest
    @MethodSource("utils.JsonTestUtils#getArgumentsForMethod")
    @DisplayName("Weather JSON data convertion to WeatherDTO")
    public void jsonResponseConversionCorrect(String json) throws JsonProcessingException {
        WeatherDTO concerted = weatherAPIService.convertToDTO(json);
        assertThat(concerted).isEqualTo(EXPECTED_WEATHER_DTO_FROM_JSON_FILE);
    }

}
