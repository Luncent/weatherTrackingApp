package unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.locations.LocationWeatherDTO;
import org.example.dto.locations.UnsavedLocationDTO;
import org.example.mappers.LocationMapper;
import org.example.model.Coordinate;
import org.example.services.WeatherAPIService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static test_constants.LocationDTOConstants.*;
import static utils.JsonTestUtils.getJsonFromFile;

@ExtendWith(MockitoExtension.class)
public class WeatherAPIServiceTest {

    @Spy
    private static LocationMapper locationMapper;
    @Spy
    private HttpClient httpClient;
    @InjectMocks
    private WeatherAPIService weatherAPIService;

    @BeforeAll
    static void setLocationMapper(){
        locationMapper = Mappers.getMapper(LocationMapper.class);
        locationMapper.setObjectMapper(new ObjectMapper());
    }

   /* @Nested
    @DisplayName("get location weather method tests")
    public class LocationWeatherTest {
        @Test
        @DisplayName("correct response handling if response is correct")
        public void correctRequestHandlingWhenResponseCorrect() throws Exception {

            HttpResponse<String> httpExpectedResponse = mock(HttpResponse.class);
            doReturn(getJsonFromFile("location_weather_data.json")).when(httpExpectedResponse).body();
            doReturn(200).when(httpExpectedResponse).statusCode();

            CompletableFuture<HttpResponse<String>> testFutureResponse = CompletableFuture.supplyAsync(() -> httpExpectedResponse);

            Mockito.doReturn(testFutureResponse).when(httpClient).sendAsync(any(), any());


            LocationWeatherDTO resultDTO = weatherAPIService
                    .getLocationWeatherByCoordinates(new Coordinate(BigDecimal.ONE, BigDecimal.ONE), 1L);

            assertThat(EXPECTED_WEATHER_DTO_FROM_JSON_FILE).isEqualTo(resultDTO);
        }

        @ParameterizedTest
        @MethodSource("unit.WeatherAPIServiceTest#getArgumentsForCorrectResponseHandlingWhenResponseHasErrors")
        @DisplayName("correct exception message when handling error response")
        public void correctResponseErrorsHandling(String expectedErrorMessage, Integer statusCode) {
            HttpResponse<String> httpExpectedResponse = mock(HttpResponse.class);
            doReturn("dummy").when(httpExpectedResponse).body();
            doReturn(statusCode).when(httpExpectedResponse).statusCode();

            CompletableFuture<HttpResponse<String>> testFutureResponse = CompletableFuture.completedFuture(httpExpectedResponse);

            Mockito.doReturn(testFutureResponse).when(httpClient).sendAsync(any(), any());

            Exception exception = assertThrows(Exception.class, () -> weatherAPIService
                    .getLocationWeatherByCoordinates(new Coordinate(BigDecimal.ONE, BigDecimal.ONE), 1L));
            String exceptionMassage = exception.getMessage();
            assertThat(exceptionMassage)
                    .as(exceptionMassage + " contains " + expectedErrorMessage)
                    .contains(expectedErrorMessage);
        }
    }*/

    @Nested
    @DisplayName("location search method tests")
    public class LocationSearchTest {

        @Test
        public void correctRequestHandlingWhenResponseCorrect() throws Exception {
            HttpResponse<String> searchResponse = mock(HttpResponse.class);
            doReturn(getJsonFromFile("searchLocationsByCityNameTestData.json")).when(searchResponse).body();
            doReturn(200).when(searchResponse).statusCode();

            CompletableFuture<HttpResponse<String>> searchResponseFuture = CompletableFuture.completedFuture(searchResponse);

            doReturn(searchResponseFuture).when(httpClient).sendAsync(any(),any());

            List<UnsavedLocationDTO> unsaved = weatherAPIService.getLocationsByCityName(CITY_NAME);

            assertThat(LOCATION_SAVE_DTO_DTO.getName()).isIn(unsaved.stream().map(UnsavedLocationDTO::name).toList());
        }

        @ParameterizedTest
        @MethodSource("unit.WeatherAPIServiceTest#getArgumentsForCorrectResponseHandlingWhenResponseHasErrors")
        public void correctResponseErrorsHandling(String expectedErrorMessage, Integer statusCode) {
            HttpResponse<String> searchResponse = mock(HttpResponse.class);
            doReturn("dummy response message").when(searchResponse).body();
            doReturn(statusCode).when(searchResponse).statusCode();

            CompletableFuture<HttpResponse<String>> searchResponseFuture = CompletableFuture.completedFuture(searchResponse);

            doReturn(searchResponseFuture).when(httpClient).sendAsync(any(),any());

            Exception exception = assertThrows(Exception.class, () -> weatherAPIService.getLocationsByCityName("dummy_city"));

            assertThat(exception.getMessage()).contains(expectedErrorMessage);
        }

    }


    public static Stream<Arguments> getArgumentsForCorrectResponseHandlingWhenResponseHasErrors() {
        return Stream.of(
                Arguments.of("Ошибка клиента", 400),
                Arguments.of("Ошибка сервера", 500)
        );
    }


}
