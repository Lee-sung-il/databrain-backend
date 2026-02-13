package com.sungil.springboot.databrainbackend.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sungil.springboot.databrainbackend.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 영어 원문 및 언어별 번역 컬럼
    private String titleEn; @Column(columnDefinition = "TEXT") private String contentEn;
    private String titleKo; @Column(columnDefinition = "TEXT") private String contentKo;
    private String titleJa; @Column(columnDefinition = "TEXT") private String contentJa;
    private String titleZh; @Column(columnDefinition = "TEXT") private String contentZh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore  // [추가] 스웨거와 JSON 변환 시 이 필드는 무시하도록 설정
    private User user;
    private LocalDateTime createdAt;

    /**
     * [수정 메서드] NoteService에서 넘겨주는 순서(En -> Ko -> Ja -> Zh)에 맞춰
     * 필드 값을 업데이트합니다.
     */
    public void updateAllLanguages(String nt, String nc, String ko, String koContent, String ja, String jaContent, String zh, String zhContent) {
        this.titleEn = nt;
        this.contentEn = nc;

        this.titleKo = ko;
        this.contentKo = koContent;

        this.titleJa = ja;
        this.contentJa = jaContent;

        this.titleZh = zh;
        this.contentZh = zhContent;
    }
}