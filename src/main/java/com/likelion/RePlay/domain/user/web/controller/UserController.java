package com.likelion.RePlay.domain.user.web.controller;

import com.likelion.RePlay.domain.user.service.UserServiceImpl;
import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserServiceImpl userService;

    //회원가입
    @PostMapping(path = "/signUp")
    private ResponseEntity<CustomAPIResponse<?>> signUp(@RequestBody @Valid UserSignUpDto userSignUpDto) {
        return userService.signUp(userSignUpDto);
    }

    //로그인
    @PostMapping(path = "/login")
    private ResponseEntity<CustomAPIResponse<?>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}