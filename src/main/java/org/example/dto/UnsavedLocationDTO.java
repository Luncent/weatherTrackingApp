package org.example.dto;

import java.math.BigDecimal;

public record UnsavedLocationDTO(String name,
                                 BigDecimal latitude,
                                 BigDecimal longitude) {
}
