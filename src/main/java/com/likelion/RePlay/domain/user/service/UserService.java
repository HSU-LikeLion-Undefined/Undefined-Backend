package com.likelion.RePlay.domain.user.service;

import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserNickNameExistDto;
import com.likelion.RePlay.domain.user.web.dto.UserPhoneExistDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpRequestDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;

public interface UserService {
    //회원가입
    ResponseEntity<CustomAPIResponse<?>> signUp(UserSignUpRequestDto userSignUpRequestDto);

    //회원가입 중 전화번호 중복체크
    ResponseEntity<CustomAPIResponse<?>> isExistPhoneId(UserPhoneExistDto userPhoneExistDto);

    //회원가입 중 닉네임 중복체크
    ResponseEntity<CustomAPIResponse<?>> isExistNickName(UserNickNameExistDto userNickNameExistDto);

    //로그인
    ResponseEntity<CustomAPIResponse<?>> login(UserLoginDto userLoginDto);

    //내정보 조회
    ResponseEntity<CustomAPIResponse<?>> getMyPage(MyUserDetailsService.MyUserDetails myUserDetails);

}
