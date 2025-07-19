package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.WeatherDTO;
import org.example.services.WeatherAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static test_constants.WeatherDTOConstants.EXPECTED_WEATHER_DTO_FROM_JSON_FILE;
import static utils.JsonTestUtils.getJsonFromFile;


@ExtendWith(MockitoExtension.class)
public class WeatherAPIServiceTest {

    @Spy
    private ObjectMapper objectMapper;
    @Spy
    private HttpClient httpClient;
    @InjectMocks
    private WeatherAPIService weatherAPIService;

    @Test
    @DisplayName("correct response handling if response is correct")
    public void correctResponseHandlingWhenCorrectResponseRecieved() throws Exception {

        HttpResponse<String> httpExpectedResponse = mock(HttpResponse.class);
        doReturn(getJsonFromFile("test_data.json")).when(httpExpectedResponse).body();
        doReturn(200).when(httpExpectedResponse).statusCode();

        CompletableFuture<HttpResponse<String>> testFutureResponse = CompletableFuture.supplyAsync(() -> httpExpectedResponse);

        Mockito.doReturn(testFutureResponse).when(httpClient).sendAsync(any(), any());


        WeatherDTO resultDTO = weatherAPIService.getLocationWeatherByCoordinates(any(), any());

        assertThat(EXPECTED_WEATHER_DTO_FROM_JSON_FILE).isEqualTo(resultDTO);
    }

    @ParameterizedTest
    @MethodSource("getArgumentsForCorrectResponseHandlingWhenResponseHasErrors")
    @DisplayName("correct exception message when handling error response")
    public void correctResponseHandlingWhenResponseHasErrors(String expectedErrorMessage, Integer statusCode) {
        HttpResponse<String> httpExpectedResponse = mock(HttpResponse.class);
        doReturn("dummy").when(httpExpectedResponse).body();
        doReturn(statusCode).when(httpExpectedResponse).statusCode();

        CompletableFuture<HttpResponse<String>> testFutureResponse = CompletableFuture.completedFuture(httpExpectedResponse);
        ;

        Mockito.doReturn(testFutureResponse).when(httpClient).sendAsync(any(), any());

        Exception exception = assertThrows(Exception.class, () -> weatherAPIService.getLocationWeatherByCoordinates(any(), any()));
        String exceptionMassage = exception.getMessage();
        assertThat(exceptionMassage)
                .as(exceptionMassage + " contains " + expectedErrorMessage)
                .contains(expectedErrorMessage);
    }

    public static Stream<Arguments> getArgumentsForCorrectResponseHandlingWhenResponseHasErrors() {
        return Stream.of(
                Arguments.of("Ошибка клиента", 400),
                Arguments.of("Ошибка сервера", 500)
        );
    }

}
