package com.likelion.RePlay.global.security;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /*
     * 사용자 이름(phoneId)으로 사용자를 로드하는 메서드
     * 사용자가 없을 경우 UsernameNotFoundException을 던짐
     *
     * @param phoneId 사용자 전화 ID
     * @return UserDetails 사용자 세부 정보
     * @throws UsernameNotFoundException 사용자 이름을 찾을 수 없을 때 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String phoneId) throws UsernameNotFoundException {
        // UserRepository를 사용하여 phoneId로 사용자를 찾습니다.
        // Optional의 orElseThrow 메서드를 사용하여 조회 결과가 null일 때 예외를 던짐
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phoneId: " + phoneId));

        // 사용자의 역할(UserRole)을 GrantedAuthority로 변환
        List<GrantedAuthority> authorities = user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName().name()))
                .collect(Collectors.toUnmodifiableList());

        // Spring Security에서 인증된 사용자 정보를 캡슐화하는 UserDetails 객체를 생성
        return new MyUserDetails(user, authorities);
    }

    /*
     * 사용자 세부 정보를 캡슐화하는 클래스
     */
    @Getter
    public static class MyUserDetails implements UserDetails {
        private final User user;
        private final List<GrantedAuthority> authorities;

        public MyUserDetails(User user, List<GrantedAuthority> authorities) {
            this.user = user;
            this.authorities = authorities;
        }

        // 사용자 전화 ID를 반환하는 메서드입니다.
        public String getPhoneId() {
            return user.getPhoneId();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getPhoneId();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}