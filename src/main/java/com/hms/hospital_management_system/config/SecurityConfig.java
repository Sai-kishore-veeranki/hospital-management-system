package com.hms.hospital_management_system.config;

import com.hms.hospital_management_system.security.JwtAuthenticationEntryPoint;
import com.hms.hospital_management_system.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter; // <--- REMOVE THIS IMPORT, no longer needed directly for the bean return type
import org.springframework.web.cors.CorsConfigurationSource; // <--- ADD THIS IMPORT IF NOT ALREADY THERE

import java.util.Arrays; // <--- ADD THIS IMPORT for Arrays.asList

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enables @PreAuthorize
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS, now expecting CorsConfigurationSource
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Handle unauthorized access
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll() // Allow authentication endpoints
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Allow Swagger UI
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .authenticationProvider(authenticationProvider) // Set our custom auth provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before UsernamePassword filter

        return http.build();
    }

    // Configure CORS for local development
    @Bean // <--- This method now returns CorsConfigurationSource
    public CorsConfigurationSource corsConfigurationSource() { // <--- CHANGED RETURN TYPE
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Use Arrays.asList to create an immutable list for allowed origins
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow your frontend origin
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source; // <--- Return the source directly
    }
}










