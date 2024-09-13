package io.practice.programming_languages_picker.config;

import io.practice.programming_languages_picker.filter.CsrfCookieFilter;
import io.practice.programming_languages_picker.filter.JWTAuthenticationFilter;
import io.practice.programming_languages_picker.filter.JWTTokenGenerateFilter;
import io.practice.programming_languages_picker.filter.JWTTokenValidatorFilter;
import io.practice.programming_languages_picker.handler.AuthenticationEntryPointHandler;
import io.practice.programming_languages_picker.model.ERole;
import io.practice.programming_languages_picker.service.UserService;
import io.practice.programming_languages_picker.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.practice.programming_languages_picker.constants.SecurityConstants.ROLE_ADMIN;

@Configuration
public class SecurityConfig{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // secures all endpoints with HTTP Basic:
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, JwtUtil jwtUtil, UserService userService, AuthenticationManager authenticationManager) throws Exception {
        try {
            CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
            requestHandler.setCsrfRequestAttributeName("_csrf");
            /*
             *  Below is the custom security configurations
             */
            // use separate login form not default one from spring boot
            // create session id and follow the configration
            // don't create default session id to spring boot SessionCreationPolicy.STATELESS
            http
                    .exceptionHandling(exceptionHandling -> exceptionHandling

                            .authenticationEntryPoint(new AuthenticationEntryPointHandler()))
                    .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            config.setAllowedMethods(Collections.singletonList("*"));
                            config.setAllowCredentials(true);
                            config.setAllowedHeaders(Collections.singletonList("*"));
                            config.setExposedHeaders(List.of("Authorization"));
                            config.setMaxAge(3600L);
                            return config;
                        }
                    }))
                    .csrf((csrf) -> csrf.disable())
                    .addFilterBefore(new JWTTokenValidatorFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class) // Validate JWT before UsernamePasswordAuthenticationFilter
                    .addFilterAfter(new JWTAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class) // Handle JWT login after UsernamePasswordAuthenticationFilter
                    .addFilterAfter(new JWTTokenGenerateFilter(), UsernamePasswordAuthenticationFilter.class) // Generate JWT token after authentication filter
                    .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class) // Add CSRF cookie after token generation
                    .authorizeHttpRequests((requests) -> requests.requestMatchers("/auth/login", "/auth/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/auth/validateToken").permitAll()
                            .requestMatchers("/api/admin/**").hasRole(ROLE_ADMIN)
                            .anyRequest().authenticated()

                    )
                   /* .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        System.out.println("Custom AuthenticationEntryPoint invoked!");
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                    })
                    .and()*/;

            return http.build();
        } catch (Exception e) {
            throw e;
        }
    }
    // it will automatically take the database from properties and use the table structure to do authentication
    // if we want custom, we use LoadUserByusername

    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)  // 1. Configures user details service
                .passwordEncoder(passwordEncoder())  // 2. Configures password encoder
                .and()
                .build();  // 3. Builds and returns the AuthenticationManager
    }
}
