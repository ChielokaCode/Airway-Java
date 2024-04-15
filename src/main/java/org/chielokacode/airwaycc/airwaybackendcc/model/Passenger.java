package org.chielokacode.airwaycc.airwaybackendcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chielokacode.airwaycc.airwaybackendcc.enums.Category;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PSN;
    @Enumerated(EnumType.STRING)
    private Category category;
    @OneToOne
    private User user;
    private String contactPhone;
    private String contactEmail;
    @ManyToMany(mappedBy = "passengers")
    private List<Flight> flights;
    // Getters and setters
}
