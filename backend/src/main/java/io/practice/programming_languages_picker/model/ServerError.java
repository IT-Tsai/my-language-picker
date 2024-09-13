package io.practice.programming_languages_picker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ServerError {

    enum ErrorCode {

    }
    @Getter
    @NotNull
    private String message;
    @Getter
    @NotNull
    private HttpStatus status;
    @Getter
    @NotNull
    private Integer statusCode;

    public ServerError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.statusCode = status.value();
    }

    public ServerError(String message) {
        this.message = message;
        this.status = HttpStatus.UNAUTHORIZED;

    }

    @Override
    public String toString() {
        return "ServerError{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", statusCode=" + statusCode +
                '}';
    }
}
