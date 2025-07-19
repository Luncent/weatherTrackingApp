package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.dto.WeatherDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WeatherAPIService {

    private final String getLocationWeatherByCoordinatesRequestTemplate;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public WeatherAPIService(final ObjectMapper objectMapper, final HttpClient httpClient,
                             @Value("weather_api_key") String apiKey) {

        this.getLocationWeatherByCoordinatesRequestTemplate = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=" + apiKey;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    public WeatherDTO getLocationWeatherByCoordinates(BigDecimal longitude, BigDecimal latitude) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(String.format(getLocationWeatherByCoordinatesRequestTemplate, latitude,longitude)))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        //json body
        //using async so that the httpclient doesn't block himself while the request is being executed,
        //which would cause other users using methods with HttpClient to be delayed.

        CompletableFuture<HttpResponse<String>> futureResponse = httpClient.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response = futureResponse.get();
        String jsonBody = handleResponseAndIfOKReturnBody(response);
        return convertToDTO(jsonBody);
        //return convertToDTO(response.get().body());
    }

    private String handleResponseAndIfOKReturnBody(HttpResponse<String> response) throws Exception {
        int statusCode = response.statusCode();
        if(is2xx(statusCode)) {
            return response.body();
        }
        if(is4xx(statusCode)) {
            throw new Exception("Ошибка клиента: "+response.body());
        }
        if(is5xx(statusCode)) {
            throw new Exception("Ошибка сервера: "+response.body());
        }
        else throw new Exception("Необрабатываемый статус код: "+statusCode);
    }

    private  boolean is2xx(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private  boolean is4xx(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    private  boolean is5xx(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }

    public WeatherDTO convertToDTO(String jsonString) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonString);

        String city = rootNode.get("name").asText();

        JsonNode coordNode = rootNode.get("coord");
        BigDecimal longitude = coordNode.get("lon").decimalValue();
        BigDecimal latitude = coordNode.get("lat").decimalValue();

        JsonNode weatherNodes = rootNode.get("weather");
        String weatherDescription = StreamSupport.stream(weatherNodes.spliterator(), false)
                .map(weatherNode->weatherNode.get("description").asText())
                .collect(Collectors.joining(","));

        JsonNode mainNode = rootNode.get("main");
        BigDecimal temperature = mainNode.get("temp").decimalValue();
        BigDecimal feelsLikeTemperature = mainNode.get("feels_like").decimalValue();
        Integer humidity = mainNode.get("humidity").asInt();

        JsonNode sysNode = rootNode.get("sys");
        String countryCode = sysNode.get("country").asText();

        return WeatherDTO.builder()
                .temperature(temperature)
                .feelsLikeTemperature(feelsLikeTemperature)
                .humidity(humidity)
                .longitude(longitude)
                .latitude(latitude)
                .countryCode(countryCode)
                .weatherDescription(weatherDescription)
                .city(city)
                .build();
    }

}
