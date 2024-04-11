package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {
    private Long id;
    private String className;
    private BigDecimal baseFare;
    private int availableSeat;
}
