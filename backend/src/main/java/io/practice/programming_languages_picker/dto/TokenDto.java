package io.practice.programming_languages_picker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

public class TokenDto {

    @Getter
    @Setter
    @NotBlank(message = "Access Token can not be blank")
    private String accessToken;

    @Getter
    @Setter
    @NotNull(message = "Access Token Expires At can not be null")
    private OffsetDateTime accessTokenExpiresAt;

    @Getter
    @Setter
    @NotBlank(message = "Refresh Token can not be blank")
    private String refreshToken;

    @Getter
    @Setter
    @NotNull(message = "Refresh Token Expires At can not be null")
    private OffsetDateTime refreshTokenExpiresAt;

    @Getter
    @Setter
    @NotBlank(message = "Email can not be blank")
    private String email;

    public TokenDto(String accessToken, OffsetDateTime accessTokenExpiresAt, String refreshToken, OffsetDateTime refreshTokenExpiresAt, String email) {
        this.accessToken = accessToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.email = email;
    }
}
