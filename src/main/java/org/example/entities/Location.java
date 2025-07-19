package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;

@Entity
@Table(name="locations")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
