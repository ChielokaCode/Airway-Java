package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.model.Country;
import org.airway.airwaybackend.serviceImpl.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CountryControllerTest {

    @Mock
    private CountryServiceImpl countryService;

    @InjectMocks
    private CountryController countryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1L, "USA", "US"));
        countries.add(new Country(2L, "Canada", "CA"));

        when(countryService.getAllCountries()).thenReturn(countries);

        ResponseEntity<List<Country>> response = countryController.getAllCountries();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countries, response.getBody());
    }

    @Test
    public void testGetCountryByIsoCode() {
        Country country = new Country(1L, "USA", "US");

        when(countryService.getCountryByIsoCode("US")).thenReturn(country);

        ResponseEntity<Country> response = countryController.getCountryByIsoCode("US");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(country, response.getBody());
    }

    @Test
    public void testGetCountryById() {
        Country country = new Country(1L, "USA", "US");

        when(countryService.getCountryById(1L)).thenReturn(country);

        ResponseEntity<Country> response = countryController.getCountryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(country, response.getBody());
    }
}
