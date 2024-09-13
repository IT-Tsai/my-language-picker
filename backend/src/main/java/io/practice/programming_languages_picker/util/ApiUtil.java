package io.practice.programming_languages_picker.util;

import org.springframework.stereotype.Component;

import org.springframework.http.HttpHeaders;

@Component
public class ApiUtil {

    public HttpHeaders getHeader(String headerValue) {

        // empty default to json
        if (headerValue.isEmpty()) {
            headerValue = "application/json";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", headerValue);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return headers;
    }
}
