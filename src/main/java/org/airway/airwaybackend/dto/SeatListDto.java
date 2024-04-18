package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SeatListDto {
    private Long id;
    private Long seatId;
    private String seatLabel;
    private Boolean occupied;
}
