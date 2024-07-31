package com.likelion.RePlay.domain.user.web.controller;

import com.likelion.RePlay.domain.user.service.UserServiceImpl;
import com.likelion.RePlay.domain.user.web.dto.*;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private ResponseEntity<CustomAPIResponse<?>> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return userService.login(userLoginRequestDto);
    }

    // 사용자 정보 조회
    @GetMapping("/getMyPage")
    private ResponseEntity<CustomAPIResponse<?>> getMyPage(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails){
        return userService.getMyPage(userDetails);
    }


    // 사용자 정보 수정
    @PostMapping("/modifyMyPage")
    private ResponseEntity<CustomAPIResponse<?>> modifyMyPage(
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails,
            @RequestPart("modifyMyPageDto") ModifyMyPageDto modifyMyPageDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){
        return userService.modifyMyPage(userDetails, modifyMyPageDto, profileImage);
    }

}