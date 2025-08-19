package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LocationData {
    private Coordinate coordinate;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
