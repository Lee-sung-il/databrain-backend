package com.sungil.springboot.databrainbackend.api.service;

import com.sungil.springboot.databrainbackend.api.domain.User;
import com.sungil.springboot.databrainbackend.api.dto.UserSignupRequest;
import com.sungil.springboot.databrainbackend.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long signUp(UserSignupRequest request) {
//        1. 중복 이메일 체크
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일 입니다.");
        }

        String defaultProfileUrl = "https://ui-avatars.com/api/?background=random&name=" + request.getNickname();
//        2. 유저 엔터티 생성 (비밀번호 암호화는 나중에 Security 추가 할떄 추가
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getNickname(),
                "USER",
                defaultProfileUrl
        );
        return userRepository.save(user).getId();
    }
}
