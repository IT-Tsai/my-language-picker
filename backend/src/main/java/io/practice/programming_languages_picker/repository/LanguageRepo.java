package io.practice.programming_languages_picker.repository;

import io.practice.programming_languages_picker.model.Language;
import io.practice.programming_languages_picker.projection.LanguageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepo extends JpaRepository<Language, Integer> {

    @Query("SELECT l.id AS id, l.name AS name, l.imageUrl AS imageUrl FROM Language l")
    List<LanguageProjection> findAllProjectedBy();

    @Query("SELECT s.id AS skillId, l.id AS id, l.name AS name, l.imageUrl AS imageUrl " +
            "FROM Skill s LEFT JOIN s.language l " +
            "WHERE s.user.id = :userId")
    List<LanguageProjection> findLanguagesByUserId(@Param("userId") Integer userId);


}
