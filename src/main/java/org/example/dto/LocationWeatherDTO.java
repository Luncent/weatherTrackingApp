package org.example.dto;

import java.math.BigDecimal;


public record LocationWeatherDTO(Long id, BigDecimal temperature, BigDecimal feelsLikeTemperature, Integer humidity,
                                 BigDecimal longitude, BigDecimal latitude, String countryCode,
                                 String weatherDescription, String city, String weatherIcon) {
}
