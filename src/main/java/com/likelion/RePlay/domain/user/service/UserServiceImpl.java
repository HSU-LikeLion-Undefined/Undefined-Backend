package com.likelion.RePlay.domain.user.service;

import com.likelion.RePlay.domain.user.entity.Role;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.entity.UserRole;
import com.likelion.RePlay.domain.user.repository.RoleRepository;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.domain.user.web.dto.*;
import com.likelion.RePlay.global.amazon.S3.S3Service;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.JwtTokenProvider;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    // 회원가입
    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> signUp(UserSignUpRequestDto userSignUpRequestDto) {
        // 전화 번호 중복 확인
        String phoneId = userSignUpRequestDto.getPhoneId();
        Optional<User> isExistPhone = userRepository.findByPhoneId(phoneId);
        if (isExistPhone.isPresent()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 번호입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }
        String nickName= userSignUpRequestDto.getNickname();
        Optional<User> isExistNickName =userRepository.findByNickname(nickName);
        if (isExistNickName.isPresent()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);

        }

        List<Role> roles = userSignUpRequestDto.getUserRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        String password = userSignUpRequestDto.getPassword();

        // 회원가입 후 결과를 반환하기 위한 dto
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto(nickName, phoneId, password);
        // 존재하지 않으면 회원가입 진행
        User newUser = User.builder()
                .phoneId(phoneId)
                .password(passwordEncoder.encode(userSignUpRequestDto.getPassword())) // 비밀번호 암호화
                .nickname(userSignUpRequestDto.getNickname())
                //.state(userSignUpRequestDto.getState())
                //.district(userSignUpRequestDto.getDistrict())
                .year(userSignUpRequestDto.getYear())
                .build();

        roles.forEach(newUser::addRole);
        userRepository.save(newUser);


        // 회원가입 성공
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), userSignUpResponseDto, "회원가입에 성공하였습니다."));
    }

    // 전화번호 중복 확인
    @Override
    public ResponseEntity<CustomAPIResponse<?>> isExistPhoneId(UserPhoneExistDto userPhoneExistDto) {
        Optional<User> user = userRepository.findByPhoneId(userPhoneExistDto.getPhoneId());

        if (user.isPresent()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 번호입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), null, "사용할 수 있는 번호입니다."));
        }
    }

    // 닉네임 중복 확인
    @Override
    public ResponseEntity<CustomAPIResponse<?>> isExistNickName(UserNickNameExistDto userNickNameExistDto) {
        Optional<User> user=userRepository.findByNickname(userNickNameExistDto.getNickName());
        if (user.isPresent()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);

        }
        else {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), null, "사용할 수 있는 닉네임입니다."));
        }

    }


    // 로그인
    @Override
    public ResponseEntity<CustomAPIResponse<?>> login(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> isExistUser = userRepository.findByPhoneId(userLoginRequestDto.getPhoneId());
        if (isExistUser.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "전화번호가 존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        User user = isExistUser.get();

        // 패스워드 불일치
        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(),
                            "비밀번호가 일치하지 않습니다."));
        }

        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.createToken(user.getPhoneId(), roles);

        // 역할 목록을 문자열 리스트로 변환
        List<String> roleNames = roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(token, roleNames);

        // 로그인 성공
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), userLoginResponseDto, "로그인에 성공하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getMyPage(MyUserDetailsService.MyUserDetails myUserDetails) {
        Optional<User> isExistUser = userRepository.findByPhoneId(myUserDetails.getPhoneId());
        if (isExistUser.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }
        String phoneId = myUserDetails.getPhoneId();
        String nickName=myUserDetails.getUsername();
        Long year=myUserDetails.getYear();
        String profileImage=myUserDetails.getUser().getProfileImage();

        GetUserPageResponseDto res=new GetUserPageResponseDto(phoneId, nickName, year, profileImage);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), res, "사용자 정보 조회에 성공하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> modifyMyPage(MyUserDetailsService.MyUserDetails myUserDetails, ModifyMyPageDto modifyMyPageDto, MultipartFile profileImage) {
        Optional<User> isExistUser = userRepository.findByPhoneId(myUserDetails.getPhoneId());
        if (isExistUser.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        User user = isExistUser.get();
        String profileImageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageUrl= s3Service.uploadFile(profileImage);
            } catch (IOException e) {
                CustomAPIResponse<Object> failResponse = CustomAPIResponse
                        .createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "프로필 이미지 업로드 실패");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failResponse);
            }
        }

        // 원래 사용하던 전화번호가 아니라면
        if(!(myUserDetails.getPhoneId().equals(modifyMyPageDto.getPhoneId()))){
            String phoneId = modifyMyPageDto.getPhoneId();
            //중복 검사
            Optional<User> isExistPhone = userRepository.findByPhoneId(phoneId);
            if (isExistPhone.isPresent()) {
                CustomAPIResponse<Object> failResponse = CustomAPIResponse
                        .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 번호입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
            }
        }

        // 원래 사용하던 닉네임이 아니라면
        if(!(myUserDetails.getUsername().equals(modifyMyPageDto.getNickName()))){
            String nickName = modifyMyPageDto.getNickName();
            Optional<User> isExistNickName = userRepository.findByNickname(nickName);
            if (isExistNickName.isPresent()) {
                CustomAPIResponse<Object> failResponse=CustomAPIResponse
                        .createFailWithout(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failResponse);
            }
        }

        user.setNickname(modifyMyPageDto.getNickName());
        user.setYear(modifyMyPageDto.getYear());
        user.setPhoneId(modifyMyPageDto.getPhoneId());
        if (profileImageUrl != null) {
            user.setProfileImage(profileImageUrl);
        }
        userRepository.save(user);


        String newToken = null;
        // 전화번호가 수정되면 새로운 토크인 발급된다
        if(!(myUserDetails.getPhoneId().equals(modifyMyPageDto.getPhoneId()))){
            newToken = jwtTokenProvider.createToken(user.getPhoneId(), user.getUserRoles().stream()
                    .map(UserRole::getRole)
                    .collect(Collectors.toList()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), newToken, "사용자 정보가 수정되었습니다."));

    }
}