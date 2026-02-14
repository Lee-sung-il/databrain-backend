package com.sungil.springboot.databrainbackend.api.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum Language {
    EN,KO,JA,ZH;

    @JsonCreator
    public static Language from(String value) {
        return Stream.of(Language.values())
                .filter(lang -> lang.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(EN); // 매칭되는 게 없으면 기본값으로 EN 설정
    }
}
