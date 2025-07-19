package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WeatherDTO {
    private BigDecimal temperature;
    private BigDecimal feelsLikeTemperature;
    private Integer humidity;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String countryCode;
    private String weatherDescription;
    private String city;
}

