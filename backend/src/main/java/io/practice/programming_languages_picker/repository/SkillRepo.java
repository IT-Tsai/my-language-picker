package io.practice.programming_languages_picker.repository;

import io.practice.programming_languages_picker.model.Skill;
import io.practice.programming_languages_picker.projection.LanguageProjection;
import io.practice.programming_languages_picker.projection.SkillProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepo extends JpaRepository<Skill, Integer> {

    @Query("SELECT s FROM Skill s WHERE s.user.id= ?1")
    List<Skill> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT s FROM Skill s WHERE s.user.id = :userId AND s.language.id = :languageId")
    Optional<Skill> findByUserIdAndLanguageId(@Param("userId") Integer userId, @Param("languageId") Integer languageId);

    @Query("SELECT s.id AS skillId, l.id AS id, l.name AS name, l.imageUrl AS imageUrl " +
            "FROM Skill s LEFT JOIN s.language l " +
            "WHERE s.user.id = :userId")
    List<SkillProjection> findSkillsByUserId(@Param("userId") Integer userId);

}
