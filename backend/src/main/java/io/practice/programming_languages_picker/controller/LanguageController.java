package io.practice.programming_languages_picker.controller;

import io.practice.programming_languages_picker.model.ServerError;
import io.practice.programming_languages_picker.service.LanguageService;
import io.practice.programming_languages_picker.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/languages")
@CrossOrigin
public class LanguageController {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private ApiUtil apiUtil;

    @GetMapping("/all")
    public ResponseEntity<?> getAllLanguages(){
        try {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("languages",languageService.getAllLanguages());

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch(BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch(Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/skill")
    public ResponseEntity<?> getLanguagesByUserId(@RequestParam("userId") Integer userId) {
        try{
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("languages",languageService.findLanguagesByUserId(userId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch(BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred." + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
