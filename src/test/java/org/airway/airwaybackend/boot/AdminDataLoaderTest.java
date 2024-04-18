package org.airway.airwaybackend.boot;

import org.airway.airwaybackend.config.SeedProperties;
import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.repository.CountryRepository;
import org.airway.airwaybackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


    class AdminDataLoaderTest {
        @Mock
        private SeedProperties seedProperties;
        @Mock
        private PasswordEncoder passwordEncoder;
        @Mock
        private UserRepository userRepository;
        @Mock
        private List<User> adminList;

        @Mock
        private CountryRepository countryRepository;
        @InjectMocks
        private AdminDataLoader adminDataLoader;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testSeedAdmin(){

            when(seedProperties.isEnabled()).thenReturn(true);

            List<User> adminList = Arrays.asList(
                    new User("Desmond", "Isama", "isamadesmond@gmail.com", "09030797493", passwordEncoder.encode("1234"), Role.ADMIN, true)
            );
            when(userRepository.findUserByUserRole(Role.ADMIN)).thenReturn(adminList);

            adminDataLoader.seedAdmin();

            verify(userRepository, times(5)).save(any(User.class));
        }

        @Test
        public void testSeedCountry() {
            when(seedProperties.isEnabled()).thenReturn(true);

            adminDataLoader.seedCountry();

            verify(countryRepository, times(54)).save(any());
        }

        @Test
        public void testSeedProperties_Enabled() {
            when(seedProperties.isEnabled()).thenReturn(true);
            adminDataLoader.seedProperties();

            assertTrue(seedProperties.isEnabled());
        }

        @Test
        public void testSeedProperties_Disabled() {
            when(seedProperties.isEnabled()).thenReturn(false);
            adminDataLoader.seedProperties();

            assertFalse(seedProperties.isEnabled());
        }



    }
