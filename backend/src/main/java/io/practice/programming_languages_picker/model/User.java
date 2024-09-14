package io.practice.programming_languages_picker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false) // Ensures uniqueness at the database level
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole role;

    @Column
    @Getter
    @Setter
    private String accessToken;

    @Column
    @Getter
    @Setter
    private OffsetDateTime accessTokenExpiresAt;

    @Column
    @Getter
    @Setter
    private String refreshToken;

    @Column
    @Getter
    @Setter
    private OffsetDateTime refreshTokenExpiresAt;

    @Getter
    @Setter
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Skill> skills = new HashSet<>();

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    // Implement the UserDetails methods
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User() {
    }

    public User(String email, String password, ERole role) {
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public User(String email, String password, ERole role, String accessToken, OffsetDateTime accessTokenExpiresAt, String refreshToken, OffsetDateTime refreshTokenExpiresAt) {
        this.password = password;
        this.email = email;
        this.role = role;
        this.accessToken = accessToken;
        this.accessTokenExpiresAt = accessTokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.refreshToken = refreshToken;
    }

    @Override
    public String getUsername() {
        return this.email; // as email is our username
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map the Role enum to a GrantedAuthority object
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
