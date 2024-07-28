package com.likelion.RePlay.domain.user.web.controller;

import com.likelion.RePlay.domain.user.service.UserServiceImpl;
import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserNickNameExistDto;
import com.likelion.RePlay.domain.user.web.dto.UserPhoneExistDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpRequestDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserServiceImpl userService;

    //회원가입
    @PostMapping(path = "/signUp")
    private ResponseEntity<CustomAPIResponse<?>> signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
        return userService.signUp(userSignUpRequestDto);
    }

    // 전화번호 중복 확인
    @PostMapping(path="/isExistPhoneId")
    private ResponseEntity<CustomAPIResponse<?>> isExistPhoneId(@RequestBody @Valid UserPhoneExistDto userPhoneExistDto) {
        return userService.isExistPhoneId(userPhoneExistDto);
    }

    // 닉네임 중복 확인
    @PostMapping(path="/isExistNickName")
    private ResponseEntity<CustomAPIResponse<?>> isExistNickName(@RequestBody @Valid UserNickNameExistDto userNickNameExistDto) {
        return userService.isExistNickName(userNickNameExistDto);
    }

    //로그인
    @PostMapping(path = "/login")
    private ResponseEntity<CustomAPIResponse<?>> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    // 사용자 정보 조회
    @GetMapping("/getMyPage")
    private ResponseEntity<CustomAPIResponse<?>> getMyPage(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails){
        return userService.getMyPage(userDetails);
    }

}