package org.example.services;

import lombok.extern.log4j.Log4j2;
import org.example.dto.locations.LocationWeatherDTO;
import org.example.dto.locations.UnsavedLocationDTO;
import org.example.mappers.LocationMapper;
import org.example.model.Coordinate;
import org.example.model.LocationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class WeatherAPIService {
    private final String getLocationWeatherByCoordinatesRequestTemplate;
    private final String getLocationsByCityNameRequestTemplate;
    private final LocationMapper locationMapper;
    private final HttpClient httpClient;

    public WeatherAPIService(final LocationMapper locationMapper, final HttpClient httpClient,
                             @Value("${weather_api_key}") String apiKey) {
        this.getLocationsByCityNameRequestTemplate = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=" + apiKey;
        this.getLocationWeatherByCoordinatesRequestTemplate = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=" + apiKey;
        this.locationMapper = locationMapper;
        this.httpClient = httpClient;
    }

    public List<LocationWeatherDTO> getLocationsWeatherByCoordinates(List<LocationData> locationDataList) throws Exception {
        List<LocationWeatherDTO> locationWeatherDTOList = new ArrayList<>();
        List<CompletableFuture<HttpResponse<String>>> futures = new LinkedList<>();

        //starting all requests non-blocking way
        for(LocationData location : locationDataList) {
            futures.add(getFutureLocation(location.getCoordinate()));
        }

        for(CompletableFuture<HttpResponse<String>> future : futures) {
            String jsonBody = getResponseBody(future.get());
            LocationWeatherDTO weatherDTO = locationMapper.mapToWeatherInfo(jsonBody);

            LocationData locationData = locationDataList.removeFirst();
            weatherDTO.setCity(locationData.getName());
            weatherDTO.setLatitude(locationData.getLatitude());
            weatherDTO.setLongitude(locationData.getLongitude());

            locationWeatherDTOList.add(weatherDTO);
        }

        return locationWeatherDTOList;
    }

    private CompletableFuture<HttpResponse<String>> getFutureLocation(Coordinate coordinate) throws URISyntaxException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(String.format(getLocationWeatherByCoordinatesRequestTemplate, coordinate.getLatitude(), coordinate.getLongitude())))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        log.debug("sending request to weather api");
        return httpClient.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString());
    }

    public List<UnsavedLocationDTO> getLocationsByCityName(String cityName) throws Exception {
        HttpRequest getLocationsByNameReq = HttpRequest.newBuilder()
                .uri(new URI(String.format(getLocationsByCityNameRequestTemplate, cityName)))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(getLocationsByNameReq, HttpResponse.BodyHandlers.ofString());
        log.debug("sending search request to weather api");
        return locationMapper.map(getResponseBody(responseFuture.get()));
    }

    private String getResponseBody(HttpResponse<String> response) throws Exception {
        int statusCode = response.statusCode();
        if (is2xx(statusCode)) {
            return response.body();
        }
        if (is4xx(statusCode)) {
            throw new Exception("Ошибка клиента: " + response.body());
        }
        if (is5xx(statusCode)) {
            throw new Exception("Ошибка сервера: " + response.body());
        } else throw new Exception("Необрабатываемый статус код: " + statusCode);
    }

    private boolean is2xx(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private boolean is4xx(int statusCode) {
        return statusCode >= 400 && statusCode < 500;
    }

    private boolean is5xx(int statusCode) {
        return statusCode >= 500 && statusCode < 600;
    }
}
