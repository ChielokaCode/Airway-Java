package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.model.Country;
import org.airway.airwaybackend.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryByIsoCode(String isoCode) {
        return  countryRepository.findByIsoCode(isoCode);
    }

    public Country getCountryById(Long id) {
        Optional<Country> optionalCountry = countryRepository.findById(id);
        return optionalCountry.orElse(null);
    }
}
