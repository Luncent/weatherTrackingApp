package org.example.dto.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationWeatherDTO {
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
