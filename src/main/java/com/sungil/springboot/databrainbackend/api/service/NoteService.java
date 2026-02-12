package com.sungil.springboot.databrainbackend.api.service;

import com.sungil.springboot.databrainbackend.api.domain.*;
import com.sungil.springboot.databrainbackend.api.dto.*;
import com.sungil.springboot.databrainbackend.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createNote(String email, NoteRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        return noteRepository.save(note).getId();
    }

    public List<NoteResponse> getMyNotes(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return noteRepository.findAllByUserOrderByCreatedAtDesc(user)
                .stream().map(NoteResponse::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public void deleteNote(Long noteId, String email) {
        Note note = noteRepository.findById(noteId).orElseThrow();
        if (!note.getUser().getEmail().equals(email)) throw new RuntimeException("권한이 없습니다.");
        noteRepository.delete(note);
    }
}