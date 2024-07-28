package com.likelion.RePlay.domain.user.service;

import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpRequestDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    //회원가입
    ResponseEntity<CustomAPIResponse<?>> signUp(UserSignUpRequestDto userSignUpRequestDto);

    //회원가입 중 전화번호 중복체크
    ResponseEntity<CustomAPIResponse<?>> isExistPhoneId(String phoneId);

    //회원가입 중 닉네임 중복체크
    ResponseEntity<CustomAPIResponse<?>> isExistNickName(String nickName);

    //로그인
    ResponseEntity<CustomAPIResponse<?>> login(UserLoginDto userLoginDto);

}
