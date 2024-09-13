package io.practice.programming_languages_picker.repository;

import io.practice.programming_languages_picker.dto.TokenDto;
import io.practice.programming_languages_picker.model.User;
import io.practice.programming_languages_picker.projection.AuthProjection;
import io.practice.programming_languages_picker.projection.LanguageProjection;
import io.practice.programming_languages_picker.projection.UserProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    @Query("SELECT u.id AS id, u.email AS email FROM User u WHERE u.email = :email")
    Optional<UserProjection> findUserProjectedByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET " +
            "u.refreshToken = :#{#user.refreshToken}, " +
            "u.refreshTokenExpiresAt = :#{#user.refreshTokenExpiresAt}, " +
            "u.accessToken = :#{#user.accessToken}, " +
            "u.accessTokenExpiresAt = :#{#user.accessTokenExpiresAt} " +
            "WHERE u.email = :#{#user.email}")
    int updateTokens(@Param("user") TokenDto user);


    @Modifying
    @Transactional
    @Query("UPDATE User u SET " +
            "u.password = CASE WHEN :#{#user.password} IS NOT NULL AND :#{#user.password} <> '' THEN :#{#user.password} ELSE u.password END " +
            "WHERE u.email = :#{#user.email}")
    void updatePassword(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET " +
            "u.refreshToken = :refreshToken, " +
            "u.refreshTokenExpiresAt = :refreshTokenExpiresAt " +
            "WHERE u.email = :email")
    void updateRefreshToken(@Param("email") String email,
                           @Param("refreshToken") String refreshToken,
                           @Param("refreshTokenExpiresAt") OffsetDateTime refreshTokenExpiresAt);
}
