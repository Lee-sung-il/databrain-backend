package com.sungil.springboot.databrainbackend.api.controller;


import com.sungil.springboot.databrainbackend.api.dto.UserSignupRequest;
import com.sungil.springboot.databrainbackend.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // 주소 : http://localhost:8080/api/users
public class UserController {

    private final UserService userService;

    //회원가입 API
    //요청 주소 : POST http://localhost:8080/users/singup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserSignupRequest request) {
        //서비스에게 회원 가입 처리 맡김
        Long userId = userService.signUp(request);

//        성공시 201 Created 상태코드와 디비깅 메시지 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("회원가입 성공! (ID : " + userId + ")");
    }
}
