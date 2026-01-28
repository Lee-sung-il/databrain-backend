package com.sungil.springboot.databrainbackend.api.repository;

import com.sungil.springboot.databrainbackend.api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    select * from user where email = ? 쿼리를 자동으로 만들어줌
    Optional<User> findByEmail(String nickname);

//    select count(*) from users where email = ?
    boolean existsByEmail(String email);
}
