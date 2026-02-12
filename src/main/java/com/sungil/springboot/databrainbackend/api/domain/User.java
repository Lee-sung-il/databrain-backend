package com.sungil.springboot.databrainbackend.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

/**
 * [User Entity]
 * 무분별한 Setter 사용을 지양하고 Builder 패턴을 지향합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Language language; // 선호 언어 (KO, EN, JA, ZH)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public User(String email, String password, String nickname, Role role, Language language) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.language = language;
    }

    public void updateLanguage(Language newLanguage) {
        this.language = newLanguage;
    }
}