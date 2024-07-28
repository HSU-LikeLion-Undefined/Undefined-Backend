package com.likelion.RePlay.domain.user.repository;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneId(String PhoneId);
    Optional<User> findByNickname(String Nickname);
}
