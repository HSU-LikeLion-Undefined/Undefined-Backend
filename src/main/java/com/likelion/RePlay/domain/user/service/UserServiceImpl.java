package com.likelion.RePlay.domain.user.service;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.domain.user.web.dto.UserLoginDto;
import com.likelion.RePlay.domain.user.web.dto.UserSignUpDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> signUp(UserSignUpDto userSignUpDto) {
        //전화 번호 중복 확인
        String phoneId=userSignUpDto.getPhoneId();

        Optional<User> idExistUser = userRepository.findByPhoneId(phoneId);
        if (idExistUser.isPresent()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 번호입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }
        // 존재하지 않으면 회원가입 진행
        User newUser = User.builder()
                .phoneId(phoneId)
                .password(passwordEncoder.encode(userSignUpDto.getPassword()))
                .nickname(userSignUpDto.getNickname())
                .state(userSignUpDto.getState())
                .district(userSignUpDto.getDistrict())
                .year(userSignUpDto.getYear())
                .build();
        userRepository.save(newUser);

        // 회원가입 성공
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), null, "회원가입에 성공하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> login(UserLoginDto userLoginDto) {
        Optional<User> idExistUser = userRepository.findByPhoneId(userLoginDto.getPhoneId());
        if (idExistUser.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "전화번호가 존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        User user = idExistUser.get();

        // 패스워드 불일치
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(),
                            "비밀번호가 일치하지 않습니다."));
        }

        // 로그인 성공
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), null, "로그인에 성공하였습니다."));
    }
}
