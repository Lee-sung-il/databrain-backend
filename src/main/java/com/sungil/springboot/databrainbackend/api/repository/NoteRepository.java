package com.sungil.springboot.databrainbackend.api.repository;

import com.sungil.springboot.databrainbackend.api.domain.Note;
import com.sungil.springboot.databrainbackend.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    // 특정 유저가 작성한 모든 노트를 최신순으로 가져오기
    List<Note> findAllByUserOrderByCreatedAtDesc(User user);
}