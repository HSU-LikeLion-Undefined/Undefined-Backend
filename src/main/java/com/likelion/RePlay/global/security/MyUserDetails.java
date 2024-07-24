package com.likelion.RePlay.global.security;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    // 사용자를 찾지 못할 경우 UsernameNotFoundException을 던진다
    public UserDetails loadUserByUsername(String phoneId) throws UsernameNotFoundException {
        // 만약 사용자를 찾지 못할 경우, RuntimeException을 던진다.
        // Optional의 orElseThrow 메서드를 사용하여 조회 결과가 null일 때 예외를 던진다.
       User user =userRepository.findByPhoneId(phoneId).orElseThrow(RuntimeException::new);

       // 각 역할(UserRole)을 GrantedAuthority로 변환
        // 여기서 SimpleGrantedAuthority 클래스는 GrantedAuthority 인터페이스의 간단한 구현체로, 권한 이름을 문자열로 받음
       List<GrantedAuthority> authorities = user.getUserRoles().stream()
               .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName().name()))
               // 변환된 권한 목록을 변경 불가능한 리스트로 수집
               .collect(Collectors.toUnmodifiableList());

       // Spring Security에서 인증된 사용자 정보를 캡슐화하는 UserDetails 객체를 생성
       return new org.springframework.security.core.userdetails.User(
               user.getPhoneId(), user.getPassword(), authorities);
    }
}
