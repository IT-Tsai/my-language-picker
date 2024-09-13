package io.practice.programming_languages_picker.controller;

import io.practice.programming_languages_picker.model.ServerError;
import io.practice.programming_languages_picker.model.User;
import io.practice.programming_languages_picker.projection.UserProjection;
import io.practice.programming_languages_picker.service.UserService;
import io.practice.programming_languages_picker.util.ApiUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApiUtil apiUtil;

    @GetMapping("/id")
    public ResponseEntity<?> getUserById(@RequestParam("userId") Integer userId) {
        try {
            Map<String, User> responseBody = new HashMap<>();
            responseBody.put("user", userService.getUserById(userId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>( new ServerError("User not found by id : " + userId, HttpStatus.NOT_FOUND), apiUtil.getHeader(""),HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        try {
            Map<String, UserProjection> responseBody = new HashMap<>();
            responseBody.put("user", userService.getUserByEmail(email));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>( new ServerError("User not found by email : " + email), apiUtil.getHeader(""),HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError("An unexpected error occurred."), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, Object> user) {
        try {
            User updatedUser = new User((String) user.get("email"),
                                        (String) user.get("password"));
            userService.updatePassword(updatedUser);

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Successfully updated password");

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(new ServerError("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") int userId) {
        try {
            userService.deleteUser(userId);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Successfully deleted user.");
            return new ResponseEntity<>(responseBody, apiUtil.getHeader("") , HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred."), apiUtil.getHeader(""), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
