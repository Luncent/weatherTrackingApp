package org.example.dto.locations;

import java.math.BigDecimal;

public record UnsavedLocationDTO(String name,
                                 BigDecimal latitude,
                                 BigDecimal longitude) {
}
