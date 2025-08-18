package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationWeatherDTO {
    private Long id;
    private BigDecimal temperature;
    private BigDecimal feelsLikeTemperature;
    private Integer humidity;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String countryCode;
    private String weatherDescription;
    private String city;
    private String weatherIcon;
}
