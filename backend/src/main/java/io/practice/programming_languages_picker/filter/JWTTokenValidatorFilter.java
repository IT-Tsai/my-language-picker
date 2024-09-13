package io.practice.programming_languages_picker.filter;

import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import io.practice.programming_languages_picker.constants.SecurityConstants;

import io.practice.programming_languages_picker.service.UserService;
import io.practice.programming_languages_picker.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static io.practice.programming_languages_picker.constants.SecurityConstants.BEARER_PREFIX;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    // Constructor injection
    public JWTTokenValidatorFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("Start validator");
            String authorizationHeader = request.getHeader(SecurityConstants.JWT_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {

                String token = authorizationHeader.substring(BEARER_PREFIX.length());
                Claims claims = jwtUtil.parseToken(token);

                String email = claims.getSubject();
                UserDetails userDetails = userService.loadUserByUsername(email);  // Fetch latest user details
                if (email != null && userDetails.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, userDetails)) {  // Validate token
                        Authentication authToken = new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }


        }
        filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException ex) {
            request.setAttribute("exception_message", "Email not found!");
            throw new BadCredentialsException("Email not found!");

        } catch (Exception ex) {
            request.setAttribute("exception_message", "Invalid Token received!");
            throw new BadCredentialsException("Invalid Token received!");
        }
    }

    @Override // do it for all api except the route created token
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals("/auth/register");
    }
}
