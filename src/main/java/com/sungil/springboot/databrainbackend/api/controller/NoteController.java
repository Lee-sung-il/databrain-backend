package com.sungil.springboot.databrainbackend.api.controller;

import com.sungil.springboot.databrainbackend.api.dto.NoteRequest;
import com.sungil.springboot.databrainbackend.api.dto.NoteResponse;
import com.sungil.springboot.databrainbackend.api.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    // 1. 노트 작성: 영어로 작성하면 백엔드에서 KO, JA, ZH로 자동 번역 저장
    @PostMapping(produces = "application/json; charset=UTF-8")
    public ResponseEntity<Long> create(@RequestBody NoteRequest request,
                                       @AuthenticationPrincipal UserDetails user) {
        Long noteId = noteService.saveNote(user.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteId);
    }

    // 2. 전체 목록 조회: 유저의 현재 언어 설정에 맞춰 자동 변환된 리스트 반환
    @GetMapping(produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<NoteResponse>> list(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(noteService.getMyNotes(user.getUsername()));
    }

    // 3. 상세 조회: 유저의 현재 언어 설정에 맞춰 자동 변환된 내용 반환
    @GetMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<NoteResponse> getDetail(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(noteService.getNote(id, user.getUsername()));
    }

    // 4. 노트 수정: 영문 원본 수정 시 모든 다국어 번역본도 함께 갱신
    @PutMapping(value = "/{id}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody NoteRequest request,
                                       @AuthenticationPrincipal UserDetails user) {
        noteService.updateNote(id, user.getUsername(), request);
        return ResponseEntity.ok().build();
    }

    // 5. 노트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetails user) {
        noteService.deleteNote(id, user.getUsername());
        return ResponseEntity.ok().build();
    }
}