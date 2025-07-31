package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LocationWeatherDTO {
    private BigDecimal temperature;
    private BigDecimal feelsLikeTemperature;
    private Integer humidity;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String countryCode;
    private String weatherDescription;
    private String city;
}

