package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripSummaryDTo {
    private List<FlightConfirmDTo> flightDetails;
    private String bookingRef;
    private String taxAmount;
    private String serviceCharge;
    private String surCharge;
    private String baseFare;
    private String totalFare;
}
