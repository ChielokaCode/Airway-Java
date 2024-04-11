package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByIsoCode(String isoCode);
}
