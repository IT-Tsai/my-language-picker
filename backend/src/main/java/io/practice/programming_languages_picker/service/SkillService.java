package io.practice.programming_languages_picker.service;

import io.practice.programming_languages_picker.model.Language;
import io.practice.programming_languages_picker.model.User;
import io.practice.programming_languages_picker.model.Skill;
import io.practice.programming_languages_picker.projection.LanguageProjection;
import io.practice.programming_languages_picker.projection.SkillProjection;
import io.practice.programming_languages_picker.repository.LanguageRepo;
import io.practice.programming_languages_picker.repository.SkillRepo;
import io.practice.programming_languages_picker.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    private SkillRepo skillRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LanguageRepo languageRepo;

    public List<Skill> getAllByUserId(Integer userId) {
        return skillRepo.findByUserId(userId);
    }

    @Transactional
    public Optional<Skill> findByUserIdAndLanguageId(Integer userId, Integer languageId) {
        return skillRepo.findByUserIdAndLanguageId(userId, languageId);
    }

    @Transactional
    public List<SkillProjection> findSkillsByUserId(Integer userId) {
        return  skillRepo.findSkillsByUserId(userId);
    }

    @Transactional
    public Skill addSkill(Integer userId, Integer languageId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("Can't find user by Id " + userId));
        Language language = languageRepo.findById(languageId).orElseThrow(() -> new EntityNotFoundException("Can't find language by Id " + languageId));

        Skill newUserSkill = new Skill(user, language);

        return skillRepo.save(newUserSkill);
    }

    public void deleteSkillById(Integer id) {
        skillRepo.deleteById(id);
    }
}
