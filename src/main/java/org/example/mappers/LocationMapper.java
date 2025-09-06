package org.example.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;
import lombok.SneakyThrows;
import org.example.dto.locations.LocationSaveDTO;
import org.example.dto.locations.LocationWeatherDTO;
import org.example.dto.locations.UnsavedLocationDTO;
import org.example.entities.Location;
import org.example.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;


@Mapper(componentModel = "spring")
@Setter
public abstract class LocationMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(saveDTO.getName())")
    @Mapping(target = "latitude", expression = "java(saveDTO.getLatitude())")
    @Mapping(target = "longitude", expression = "java(saveDTO.getLongitude())")
    @Mapping(target = "user", source = "user")
    public abstract Location map(LocationSaveDTO saveDTO, User user);

    public LocationWeatherDTO mapToWeatherInfo(String jsonString) throws JsonProcessingException {
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
        String weatherIcon = firstWeather.get("icon").asText();

        JsonNode mainNode = rootNode.get("main");
        BigDecimal temperature = mainNode.get("temp").decimalValue();
        BigDecimal feelsLikeTemperature = mainNode.get("feels_like").decimalValue();
        Integer humidity = mainNode.get("humidity").asInt();

        JsonNode sysNode = rootNode.get("sys");
        String countryCode = sysNode.get("country") != null ? sysNode.get("country").asText() : "(Absent)";

        return new LocationWeatherDTO(
                temperature, feelsLikeTemperature, humidity ,longitude, latitude ,countryCode ,weatherDescription, city, weatherIcon
        );
    }

    public List<UnsavedLocationDTO> map(String json) throws JsonProcessingException {
        JsonNode locations = objectMapper.readTree(json);
        return StreamSupport.stream(locations.spliterator(), false)
                .map(this::map)
                .toList();
    }

    @SneakyThrows
    private UnsavedLocationDTO map(JsonNode location){
        String name = location.get("name").asText();
        BigDecimal longitude = location.get("lon").decimalValue();
        BigDecimal latitude = location.get("lat").decimalValue();

        return new UnsavedLocationDTO(name, latitude, longitude);
    }
}
