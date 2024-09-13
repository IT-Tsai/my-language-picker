package io.practice.programming_languages_picker.service;

import io.practice.programming_languages_picker.projection.LanguageProjection;
import io.practice.programming_languages_picker.repository.LanguageRepo;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

    @Autowired
    private LanguageRepo languageRepo;

    private static final Logger logger = LoggerFactory.getLogger(LanguageRepo.class);

    public List<LanguageProjection>  getAllLanguages(){
        List<LanguageProjection> languages = languageRepo.findAllProjectedBy();
        logger.info("Retrieved languages: {}", languages);

        return languages;
    }

    public List<LanguageProjection> findLanguagesByUserId(Integer userId) {
        return  languageRepo.findLanguagesByUserId(userId);
    }
}
