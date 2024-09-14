package io.practice.programming_languages_picker.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "languages")
public class Language {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String name;

    @Setter
    @Getter
    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Skill> skills = new HashSet<>();

    public Language() {
    }

    public Language(String name, String image_url) {
        this.name = name;
        this.imageUrl = image_url;
    }


    public Set<Skill> getSkills() {
        return Set.copyOf(skills);
    }


    @Override
    public String toString() {
        return "ProgrammingLanguage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

