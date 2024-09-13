package io.practice.programming_languages_picker.projection;

import io.practice.programming_languages_picker.model.Language;
import io.practice.programming_languages_picker.model.User;

public interface SkillProjection {
    Integer getSkillId();
    Integer getId();
    String getName();
    String getImageUrl();
}
