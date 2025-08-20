package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authorization {
    private Long id;
    private String username;
}
