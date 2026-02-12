package com.sungil.springboot.databrainbackend.api.dto;

import com.sungil.springboot.databrainbackend.api.domain.Language;
import com.sungil.springboot.databrainbackend.api.domain.Note;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoteResponse {
    private Long id;
    private String title;
    private String content;

    public static NoteResponse fromEntity(Note note, Language lang) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(lang == Language.KO ? note.getTitleKo() :
                        lang == Language.JA ? note.getTitleJa() :
                                lang == Language.ZH ? note.getTitleZh() : note.getTitleEn())
                .content(lang == Language.KO ? note.getContentKo() :
                        lang == Language.JA ? note.getContentJa() :
                                lang == Language.ZH ? note.getContentZh() : note.getContentEn())
                .build();
    }
}