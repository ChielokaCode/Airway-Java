package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.model.Country;
import org.airway.airwaybackend.serviceImpl.CountryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class CountryController {

    private final CountryServiceImpl countryService;

    @Autowired
    public CountryController(CountryServiceImpl countryService) {
        this.countryService = countryService;
    }
    @GetMapping("/get-all-countries")
    public ResponseEntity<List<Country>> getAllCountries(){
        List<Country> countries = countryService.getAllCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }


    @GetMapping("/get-by-iso/{isoCode}")
    public ResponseEntity<Country> getCountryByIsoCode(@PathVariable String isoCode){
        Country country = countryService.getCountryByIsoCode(isoCode);
        return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
    }

    @GetMapping("/get-country/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id){
        Country country = countryService.getCountryById(id);
        return country != null ? ResponseEntity.ok(country) : ResponseEntity.notFound().build();
    }
}
