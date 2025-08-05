package org.example.dto.requests_dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationSaveDTO {
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
