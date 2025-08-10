package org.example.dto;

import lombok.*;

import java.math.BigDecimal;


public record LocationWeatherDTO(BigDecimal temperature, BigDecimal feelsLikeTemperature, Integer humidity,
                                 BigDecimal longitude, BigDecimal latitude, String countryCode,
                                 String weatherDescription, String city) {
}

