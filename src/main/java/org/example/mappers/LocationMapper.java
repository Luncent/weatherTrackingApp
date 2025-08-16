package org.example.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.example.dto.LocationWeatherDTO;
import org.example.dto.UnsavedLocationDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@AllArgsConstructor
public class LocationMapper {

    private final ObjectMapper objectMapper;


    public LocationWeatherDTO convertToLocationWeatherDTO(String jsonString, Long locationId) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonString);

        String city = rootNode.get("name").asText();

        JsonNode coordNode = rootNode.get("coord");
        BigDecimal longitude = coordNode.get("lon").decimalValue();
        BigDecimal latitude = coordNode.get("lat").decimalValue();

        JsonNode weatherNodes = rootNode.get("weather");
        JsonNode firstWeather = StreamSupport.stream(weatherNodes.spliterator(),false)
                .findFirst()
                .orElseThrow(()->new EntityNotFoundException("weather description not found"));
        String weatherDescription = firstWeather.get("description").asText();
                //.collect(Collectors.joining(","));
        String weatherIcon = firstWeather.get("icon").asText();

        JsonNode mainNode = rootNode.get("main");
        BigDecimal temperature = mainNode.get("temp").decimalValue();
        BigDecimal feelsLikeTemperature = mainNode.get("feels_like").decimalValue();
        Integer humidity = mainNode.get("humidity").asInt();

        JsonNode sysNode = rootNode.get("sys");
        String countryCode = sysNode.get("country").asText();

        return new LocationWeatherDTO(
                locationId, temperature, feelsLikeTemperature, humidity ,longitude, latitude ,countryCode ,weatherDescription, city, weatherIcon
        );
    }

    public List<UnsavedLocationDTO> convertToLocationDTOList(String json) throws JsonProcessingException {
        JsonNode locations = objectMapper.readTree(json);
        return StreamSupport.stream(locations.spliterator(), false)
                .map(this::convertToLocationDTO)
                .toList();
    }

    @SneakyThrows
    private UnsavedLocationDTO convertToLocationDTO(JsonNode location){
        String name = location.get("name").asText();
        BigDecimal longitude = location.get("lon").decimalValue();
        BigDecimal latitude = location.get("lat").decimalValue();

        return new UnsavedLocationDTO(name, latitude, longitude);
    }
}
