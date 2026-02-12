package com.sungil.springboot.databrainbackend.api.service;

import com.sungil.springboot.databrainbackend.api.domain.*;
import com.sungil.springboot.databrainbackend.api.dto.*;
import com.sungil.springboot.databrainbackend.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TranslationService translationService;

    /**
     * [작성] 어떤 언어로 써도 자동 감지 후 4개 국어 번역 저장
     */
    @Transactional
    public Long saveNote(String email, NoteRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String originalTitle = request.getTitle();
        String originalContent = request.getContent();

        // 1. 입력된 글의 언어를 감지 (예: "안녕" -> "ko")
        String sourceLang = translationService.detectLanguage(originalTitle);

        // 2. 감지된 언어를 기준으로 4개 국어 빌드
        Note note = Note.builder()
                .titleEn(translationService.translate(originalTitle, sourceLang, "en"))
                .contentEn(translationService.translate(originalContent, sourceLang, "en"))
                .titleKo(translationService.translate(originalTitle, sourceLang, "ko"))
                .contentKo(translationService.translate(originalContent, sourceLang, "ko"))
                .titleJa(translationService.translate(originalTitle, sourceLang, "ja"))
                .contentJa(translationService.translate(originalContent, sourceLang, "ja"))
                .titleZh(translationService.translate(originalTitle, sourceLang, "zh"))
                .contentZh(translationService.translate(originalContent, sourceLang, "zh"))
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return noteRepository.save(note).getId();
    }

    /**
     * [조회] 유저의 프로필 언어 설정에 따라 자동으로 데이터 필터링 반환
     */
    public List<NoteResponse> getMyNotes(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Language userLang = user.getLanguage();

        return noteRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(note -> NoteResponse.fromEntity(note, userLang))
                .toList();
    }

    public NoteResponse getNote(Long id, String email) {
        Note note = noteRepository.findById(id).orElseThrow();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (!note.getUser().getEmail().equals(email)) throw new RuntimeException("권한이 없습니다.");

        return NoteResponse.fromEntity(note, user.getLanguage());
    }

    /**
     * [수정] 수정 시에도 언어를 다시 감지해 모든 번역본 최신화
     */
    @Transactional
    public void updateNote(Long id, String email, NoteRequest request) {
        Note note = noteRepository.findById(id).orElseThrow();
        if (!note.getUser().getEmail().equals(email)) throw new RuntimeException("수정 권한이 없습니다.");

        String nt = request.getTitle();
        String nc = request.getContent();

        // 수정된 내용의 언어를 다시 감지
        String sourceLang = translationService.detectLanguage(nt);

        // 엔티티의 더티 체킹을 이용한 업데이트
        note.updateAllLanguages(
                translationService.translate(nt, sourceLang, "en"), translationService.translate(nc, sourceLang, "en"),
                translationService.translate(nt, sourceLang, "ko"), translationService.translate(nc, sourceLang, "ko"),
                translationService.translate(nt, sourceLang, "ja"), translationService.translate(nc, sourceLang, "ja"),
                translationService.translate(nt, sourceLang, "zh"), translationService.translate(nc, sourceLang, "zh")
        );
    }

    @Transactional
    public void deleteNote(Long id, String email) {
        Note note = noteRepository.findById(id).orElseThrow();
        if (!note.getUser().getEmail().equals(email)) throw new RuntimeException("삭제 권한이 없습니다.");
        noteRepository.delete(note);
    }
}