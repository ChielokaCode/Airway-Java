package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.model.Country;
import org.airway.airwaybackend.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllCountries() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country(1L, "Nigeria", "NG"));
        countries.add(new Country(2L, "Sudan", "SD"));

        when(countryRepository.findAll()).thenReturn(countries);
        List<Country> result = countryService.getAllCountries();
        assertEquals(countries.size(), result.size());
        assertEquals(countries.get(0), result.get(0));
        assertEquals(countries.get(1), result.get(1));
    }

    @Test
    public void testGetCountryByIsoCode() {
        Country country = new Country(1L, "Brazil", "BR");
        when(countryRepository.findByIsoCode("BR")).thenReturn(country);
        Country result = countryService.getCountryByIsoCode("BR");
        assertEquals(country, result);
    }

    @Test
    public void testGetCountryById_WhenCountryExists() {
        Country country = new Country(1L, "Denmark", "DK");
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        Country result = countryService.getCountryById(1L);
        assertEquals(country, result);
    }

    @Test
    public void testGetCountryById_WhenCountryDoesNotExist() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());
        Country result = countryService.getCountryById(1L);
        assertEquals(null, result);
    }
}
