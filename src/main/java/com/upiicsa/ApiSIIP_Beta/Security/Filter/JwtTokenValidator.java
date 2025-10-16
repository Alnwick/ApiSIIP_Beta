package com.upiicsa.ApiSIIP_Beta.Security.Filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.upiicsa.ApiSIIP_Beta.Model.UserSIIP;
import com.upiicsa.ApiSIIP_Beta.Repository.UserRepository;
import com.upiicsa.ApiSIIP_Beta.Utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter {

    private UserRepository userRepository;

    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);
                String email = jwtUtils.extractUsername(decodedJWT);

                // If the token is valid and there is no previous authenticate in the context
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load the user from the Data Base
                    UserSIIP user = userRepository.findByEmail(email).orElse(null);

                    if (user != null) {
                        // Created an authentication object with the user details
                        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                                user.getAuthorities());
                        //Set authentication in the security context
                        SecurityContext context = SecurityContextHolder.getContext();
                        context.setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error to validate token" + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
