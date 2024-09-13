package io.practice.programming_languages_picker.service;

import io.practice.programming_languages_picker.dto.TokenDto;
import io.practice.programming_languages_picker.model.User;
import io.practice.programming_languages_picker.projection.AuthProjection;
import io.practice.programming_languages_picker.projection.UserProjection;
import io.practice.programming_languages_picker.repository.LanguageRepo;
import io.practice.programming_languages_picker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;

    private static final Logger logger = LoggerFactory.getLogger(LanguageRepo.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities() // Authorities/roles should match what is stored in the database
        );
    }

    public int login(TokenDto loginUser) {
        return userRepo.updateTokens(loginUser);
    }

    public User register(User newUser) {
        return userRepo.save(newUser);
    }

    public User getUserById(Integer userId) {
        return userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Can't find user by Id " + userId));
    }

    public UserProjection getUserByEmail(String email) {
        return userRepo.findUserProjectedByEmail(email).orElseThrow(() -> new EntityNotFoundException("Can't find user by email " + email));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Incorrect email."));
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Transactional
    public void updatePassword(User user) {
        userRepo.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Can't find user by Id " + user.getId()));
        logger.info("updated user: {}", user);
        userRepo.updatePassword(user);
    }

    @Transactional
    public void updateRefreshToken(String email, String refreshToken, OffsetDateTime refreshTokenExpiresAt) {
        userRepo.updateRefreshToken(email, refreshToken, refreshTokenExpiresAt);
    }

    public void deleteUser(int id) {
        logger.info("delete user id: {}", id);
        userRepo.deleteById(id);
    }

}
