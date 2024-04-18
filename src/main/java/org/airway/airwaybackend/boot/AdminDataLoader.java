package org.airway.airwaybackend.boot;
import jakarta.annotation.PostConstruct;
import org.airway.airwaybackend.config.SeedProperties;
import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.model.Country;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.repository.CountryRepository;
import org.airway.airwaybackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

    @Service
    public class AdminDataLoader {
        private UserRepository userRepository;
        private SeedProperties seedProperties;
        private PasswordEncoder passwordEncoder;
        private CountryRepository countryRepository;

        @Autowired
        public AdminDataLoader(UserRepository userRepository, SeedProperties seedProperties, PasswordEncoder passwordEncoder, CountryRepository countryRepository) {
            this.userRepository = userRepository;
            this.seedProperties = seedProperties;
            this.passwordEncoder = passwordEncoder;
            this.countryRepository = countryRepository;
        }
        private  List<User> adminList;

        @PostConstruct
        void seedProperties(){
            if (seedProperties.isEnabled()){
                seedAdmin();
                seedCountry();
            }
        }

        public void seedAdmin(){
            adminList = userRepository.findUserByUserRole(Role.ADMIN);
            List<User> adminData = Arrays.asList(
                    new User("Desmond", "Isama", "isamadesmond@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true),
                    new User("James", "Adedini", "jamesadedini@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true),
                    new User("Sobowale", "Omotayo", "sobowaleomotayo97@gmail.com", "09058503787", passwordEncoder.encode("1234"), Role.ADMIN, true),
                    new User("Silas", "Bush", "silasbush@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true),
                    new User("Confidence", "Obieshi", "confidenceobieshika@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true),
//                    new User("Chieloka", "Madubugwu", "elokamadubugwu7@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true),
                    new User("Emmanuel", "Bobade", "bobmanuelbesot2@gmail.com", "08130229749", passwordEncoder.encode("1234"), Role.ADMIN, true));

            adminData.stream()
                    .filter(user -> !containsEmail(adminList, user.getEmail()))
                    .forEach(user -> userRepository.save(user));
        }

        public void seedCountry() {
            countryRepository.deleteAll();
            String[][] countriesData = {
                    {"Afghanistan", "AF"},
                    {"Algeria", "DZ"},
                    {"Angola", "AO"},
                    {"Australia", "AU"},
                    {"Austria", "AT"},
                    {"Bangladesh", "BD"},
                    {"Belgium", "BE"},
                    {"Brazil", "BR"},
                    {"Canada", "CA"},
                    {"China", "CN"},
                    {"Colombia", "CO"},
                    {"Czech Republic", "CZ"},
                    {"Denmark", "DK"},
                    {"Dominican Republic", "DO"},
                    {"Egypt", "EG"},
                    {"Ethiopia", "ET"},
                    {"France", "FR"},
                    {"Germany", "DE"},
                    {"Ghana", "GH"},
                    {"Greece", "GR"},
                    {"Hungary", "HU"},
                    {"India", "IN"},
                    {"Indonesia", "ID"},
                    {"Iran", "IR"},
                    {"Iraq", "IQ"},
                    {"Italy", "IT"},
                    {"Japan", "JP"},
                    {"Kenya", "KE"},
                    {"Madagascar", "MG"},
                    {"Mexico", "MX"},
                    {"Morocco", "MA"},
                    {"Netherlands", "NL"},
                    {"New Zealand", "NZ"},
                    {"Nigeria", "NG"},
                    {"Pakistan", "PK"},
                    {"Papua New Guinea", "PG"},
                    {"Philippines", "PH"},
                    {"Poland", "PL"},
                    {"Romania", "RO"},
                    {"Russia", "RU"},
                    {"Saudi Arabia", "SA"},
                    {"South Africa", "ZA"},
                    {"South Korea", "KR"},
                    {"Spain", "ES"},
                    {"Sudan", "SD"},
                    {"Sweden", "SE"},
                    {"Switzerland", "CH"},
                    {"Taiwan", "TW"},
                    {"Thailand", "TH"},
                    {"Turkey", "TR"},
                    {"Ukraine", "UA"},
                    {"United Kingdom", "GB"},
                    {"United States", "US"},
                    {"Vietnam", "VN"}
            };
            for (String[] countryData : countriesData) {
                Country country = new Country();
                country.setName(countryData[0]);
                country.setIsoCode(countryData[1]);
                countryRepository.save(country);
            }
        }

        private boolean containsEmail(List<User> ListOfAdmin, String email){
            return ListOfAdmin.stream()
                    .anyMatch(user -> user.getEmail().equals(email));
        }



    }
