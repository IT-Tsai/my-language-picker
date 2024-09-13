package io.practice.programming_languages_picker.projection;

import java.time.OffsetDateTime;

public interface AuthProjection {
    String getAccessToken();
    OffsetDateTime getAccessTokenExpiresAt();
    String getRefreshToken();
    OffsetDateTime getRefreshTokenExpiresAt();
}
