package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Airport {

    @Id
    private String iataCode;
    private String name;
    private String icaoCode;
    private String city;
    private String operationalHrs;
    private String state;

@JsonIgnore
    @ManyToMany(mappedBy = "airports", fetch = FetchType.EAGER)
    private Set<Airline> airlines;


    public Airport(String value, String value1, String value2, String value3, String value4, String value5) {
    }

}