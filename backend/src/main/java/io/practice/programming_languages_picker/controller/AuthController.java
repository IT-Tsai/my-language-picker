package io.practice.programming_languages_picker.controller;


import io.jsonwebtoken.Claims;
import io.practice.programming_languages_picker.dto.TokenDto;
import io.practice.programming_languages_picker.dto.UserRegistrationDto;
import io.practice.programming_languages_picker.model.ERole;
import io.practice.programming_languages_picker.model.ServerError;
import io.practice.programming_languages_picker.model.User;
import io.practice.programming_languages_picker.repository.LanguageRepo;
import io.practice.programming_languages_picker.service.UserService;
import io.practice.programming_languages_picker.util.ApiUtil;
import io.practice.programming_languages_picker.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.*;

import static io.practice.programming_languages_picker.util.DatetimeUtil.userZoneId;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(LanguageRepo.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApiUtil apiUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestedUser) {

        try {
            String email = requestedUser.get("email");
            String password = requestedUser.get("password");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate the access and refresh tokens
            String accessToken = jwtUtil.generateAccessToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // Get expiration dates
            OffsetDateTime accessTokenExpiresAt = jwtUtil.getExpirationDateFromToken(accessToken);
            OffsetDateTime refreshTokenExpiresAt = jwtUtil.getExpirationDateFromToken(refreshToken);

            // to do return token information
            User user = userService.findByEmail(email);

            userService.login(new TokenDto(
                    accessToken,
                    accessTokenExpiresAt,
                    refreshToken,
                    refreshTokenExpiresAt,
                    email));

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("email", user.getEmail());
            responseBody.put("id", user.getId());
            responseBody.put("accessToken", accessToken);
            responseBody.put("accessTokenExpiresAt", accessTokenExpiresAt.atZoneSameInstant(userZoneId));
            responseBody.put("refreshToken", refreshToken);
            responseBody.put("refreshTokenExpiresAt", refreshTokenExpiresAt.atZoneSameInstant(userZoneId));

            return new ResponseEntity<>( responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.NOT_FOUND), apiUtil.getHeader(""), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto requestedUser) {
        try {
            String encodedPassword = passwordEncoder.encode(requestedUser.getPassword());
            String email = requestedUser.getEmail();

            // Check if a user with the given email or username already exists
            if (userService.existsByEmail(email)) {
                return new ResponseEntity<>(new ServerError("Email is already taken", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    email,
                    encodedPassword,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + requestedUser.getRole()))
            );

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate the access and refresh tokens
            String accessToken = jwtUtil.generateAccessToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // Get expiration dates
            OffsetDateTime accessTokenExpiresAt = jwtUtil.getExpirationDateFromToken(accessToken);
            OffsetDateTime refreshTokenExpiresAt = jwtUtil.getExpirationDateFromToken(refreshToken);

            User newUser = new User(email, encodedPassword, Enum.valueOf(ERole.class, requestedUser.getRole()), accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);

            newUser = userService.register(newUser);

            logger.info(newUser + " new user");

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("email", newUser.getEmail());
            responseBody.put("id", newUser.getId());
            responseBody.put("accessToken", accessToken);
            responseBody.put("accessTokenExpiresAt", accessTokenExpiresAt.atZoneSameInstant(userZoneId));
            responseBody.put("refreshToken", refreshToken);
            responseBody.put("refreshTokenExpiresAt", refreshTokenExpiresAt.atZoneSameInstant(userZoneId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.BAD_REQUEST), apiUtil.getHeader(""), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("accessToken") String accessToken) {
        Map<String, Object> responseBody = new HashMap<>();
        try {
            jwtUtil.parseToken(accessToken);
            responseBody.put("isValid", true);

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);

        } catch (Exception ex) {
            responseBody.put("isValid", false);
            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        }
    }

    @PutMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, Object> body, Authentication authentication) {

        try {
            String email = (String) body.get("email");
            String refreshToken = (String) body.get("refreshToken");

            Claims claims = jwtUtil.parseToken(refreshToken);

            if (!claims.getSubject().equals(email)) {
                throw new BadCredentialsException("Bad Credential");
            }

            String newRefreshToken = jwtUtil.generateRefreshToken(authentication);
            OffsetDateTime newRefreshTokenExpiresAt = jwtUtil.getExpirationDateFromToken(newRefreshToken);

            userService.updateRefreshToken(email, newRefreshToken, newRefreshTokenExpiresAt);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("refreshToken", newRefreshToken);
            responseBody.put("refreshTokenExpiresAt", newRefreshTokenExpiresAt.atZoneSameInstant(userZoneId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.NOT_FOUND), apiUtil.getHeader(""), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
