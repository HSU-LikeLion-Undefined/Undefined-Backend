package com.likelion.RePlay.domain.user.service;

import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    //회원가입
    ResponseEntity<CustomAPIResponse<?>> signUp(UserSignUpDto userSignUpDto);

    //로그인
    ResponseEntity<CustomAPIResponse<?>> login(UserLoginDto userLoginDto);
}
