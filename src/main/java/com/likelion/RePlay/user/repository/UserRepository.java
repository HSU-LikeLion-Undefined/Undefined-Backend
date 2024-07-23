package com.likelion.RePlay.user.repository;

import com.likelion.RePlay.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneId(String phoneId);
}
