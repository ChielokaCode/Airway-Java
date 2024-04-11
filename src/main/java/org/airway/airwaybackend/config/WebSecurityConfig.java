package org.airway.airwaybackend.config;

import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.airway.airwaybackend.utils.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    private UserServiceImpl userService;
    private JwtAuthenticationFilter authentication;


    @Autowired
    public WebSecurityConfig(@Lazy UserServiceImpl userService, JwtAuthenticationFilter authentication) {
        this.userService = userService;
        this.authentication = authentication;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean//bcryptPasswordEncoder is enabled for spring security hashing/salting of user's password information
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //AuthenticationProvider(DAOAuthenticationProvider) is enabled to function as the "bouncer" in our application. Checking
    //password and User information credibility.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
        return daoAuthenticationProvider;
    }


    @Bean//Creating our authorisation security for providing the right authorisation process
    // from before "logging in" till after "logging out"
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
//                                .requestMatchers("").hasAuthority(String.valueOf(Role.ADMIN))
                                .requestMatchers(
                                        "/api/v1/auth/**","/api/v1/flights/availableFlight", "/api/v1/flights/fetch-all-flights", "/api/v1/auth/passenger-sign-up", "/api/v1/auth/verifyRegistration",
                                        "/api/v1/booking/**", "/api/v1/auth/changePassword", "/api/v1/countries/get-all-countries", "/api/v1/countries/get-country/{id}", "/api/v1/countries/get-by-iso/{isoCode}",
                                        "/api/v1/seat/get-SeatList/{seatId}","/api/v1/flights/departing-flights", "/api/v1/flights/returning-flights", "/api/v1/flights/all-returning-flights","/api/v1/flights/all-departing-flights",
                                        "/api/v1/booking/bookings","/api/v1/bookings/ticket-confirmation/{bookingRef}","/api/v1/bookings/ticket-confirmation/{token}", "/api/v1/booking/booking-flight","/api/v1/flights/{Id}", "/airports/**",
                                        "/api/v1/booking/trip-summary/{token}","/api/v1/booking/**","/api/v1/payment/**","/api/v1/flights/delete-flight/{Id}","/api/v1/flights/confirm/{Id}","/api/v1/booking/edit-bookings/{id}",
                                        "/api/v1/flights/add-flight","/api/v1/flights/update-flight/{flight-Id}", "/api/v1/flights/delete-flight/{Id}","/api/v1/user/**","/api/v1/passenger/get-passengers").permitAll()
                                .requestMatchers("/api/v1/auth/logout").authenticated())
                .logout(logout -> logout
                        .deleteCookies("remove")
                        .invalidateHttpSession(true)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authentication, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}


