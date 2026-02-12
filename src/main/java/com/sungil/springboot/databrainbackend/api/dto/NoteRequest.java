package com.sungil.springboot.databrainbackend.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoteRequest {
    private String title;
    private String content;
}
