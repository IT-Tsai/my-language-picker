package io.practice.programming_languages_picker.controller;

import io.practice.programming_languages_picker.model.ServerError;
import io.practice.programming_languages_picker.model.Skill;
import io.practice.programming_languages_picker.service.SkillService;
import io.practice.programming_languages_picker.util.ApiUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private ApiUtil apiUtil;

    @GetMapping("/")
    public ResponseEntity<?> getAllSkillByUserId(@RequestParam("userId") Integer userId) {
        try {
            Map<String, List<Skill>> responseBody = new HashMap<>();
            responseBody.put("skills",skillService.getAllByUserId(userId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError( "An unexpected error occurred."), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/skill")
    public ResponseEntity<?> getLanguagesByUserId(@RequestParam("userId") Integer userId) {
        try{
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("skills",skillService.findSkillsByUserId(userId));

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ServerError("An unexpected error occurred." + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addUserSkill(@RequestBody Map<String, Integer> ids) {
        try {
            Map<String, Integer> responseBody = new HashMap<>();

            responseBody.put("id", skillService.addSkill(ids.get("userId"), ids.get("languageId")).getId());

            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.CREATED);
        } catch(EntityNotFoundException ex) {
            return new ResponseEntity<>( new ServerError("User or Language not found.", HttpStatus.NOT_FOUND), apiUtil.getHeader(""),HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>( new ServerError("You already have this language skill.", HttpStatus.BAD_REQUEST), apiUtil.getHeader(""),HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError("An unexpected error occurred.",HttpStatus.INTERNAL_SERVER_ERROR), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/skill/delete")
    public ResponseEntity<?> deleteSkillById(@RequestBody Map<String, Integer> ids) {
        try {
            Map<String, String> responseBody = new HashMap<>();
            skillService.deleteSkillById(ids.get("skillId"));
            responseBody.put("message", "Successfully remove skill.");
            return new ResponseEntity<>(responseBody, apiUtil.getHeader(""), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError("An unexpected error occurred."), apiUtil.getHeader(""),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/existingSkill")
    public ResponseEntity<?> getSkillByUserIdANDLanguageId(@RequestParam("userId") Integer userId, @RequestParam("languageId") Integer languageId) {
        try {
            Optional<Skill> skill = skillService.findByUserIdAndLanguageId(userId, languageId);

            if(skill.isPresent()) throw new RuntimeException("Skill already exists.");

            return new ResponseEntity<>(new HashMap<>(), apiUtil.getHeader(""), HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(new ServerError(ex.getMessage(), HttpStatus.UNAUTHORIZED), apiUtil.getHeader(""),HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            return new ResponseEntity<>( new ServerError(ex.getMessage()), apiUtil.getHeader(""),HttpStatus.BAD_REQUEST);
        }
    }
}
