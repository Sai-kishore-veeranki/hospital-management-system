package com.hms.hospital_management_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList; // <--- ENSURE THIS IMPORT IS PRESENT
import java.util.Collection; // <--- ENSURE THIS IMPORT IS PRESENT
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <--- ENSURE THIS IMPORT IS PRESENT

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userName = jwtService.extractUsername(jwt);

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // --- CRITICAL CHANGE FOR ROLE EXTRACTION ---
                String roleFromJwt = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                if (roleFromJwt != null) {
                    authorities.add(new SimpleGrantedAuthority(roleFromJwt));
                    System.out.println("DEBUG: JwtAuthFilter: Extracted role from JWT: " + roleFromJwt); // DEBUG LINE
                } else {
                    // Fallback: If role is not in JWT, get from UserDetails (less ideal, but for robustness)
                    System.out.println("DEBUG: JwtAuthFilter: Role not found in JWT. Using authorities from UserDetails."); // DEBUG LINE
                    authorities.addAll(userDetails.getAuthorities().stream()
                            .map(ga -> new SimpleGrantedAuthority(ga.getAuthority()))
                            .collect(java.util.stream.Collectors.toList()));
                }
                // --- END OF CRITICAL CHANGE ---

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities // <--- THIS IS THE KEY: Use the authorities derived from JWT/UserDetails
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("DEBUG: JwtAuthFilter: Authentication set for user: " + userName + " with authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities()); // DEBUG LINE
            }
        }
        filterChain.doFilter(request, response);
    }
}