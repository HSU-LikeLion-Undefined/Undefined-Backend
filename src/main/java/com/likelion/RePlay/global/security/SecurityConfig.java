package com.likelion.RePlay.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (CSRF: 악의적인 웹 사이트가 사용자를 대신해 요청을 보내는 공격) -> RESTful API 서버에서 필요 없음
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP 기본 인증을 비활성화 (기본 인증은 브라우저의 기본 로그인 창을 사용하는 방식) -> 사용자 친화적인 로그인 폼 사용을 위해
                .formLogin(formLogin -> formLogin.disable()) // 기본 로그인 폼을 비활성화 -> 커스텀 로그인 폼 사용
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/user/signUp", "/api/user/login").permitAll() // "/signup", "/", "/login" 경로는 누구나 접근할 수 있도록 설정
                        .requestMatchers("/admin").hasRole("ADMIN") // "/admin"은 ADMIN 권한을 가진 경우에만 허용
                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // 로그아웃 후 이동할 페이지를 /login으로 설정
                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효화 -> 사용자의 세션 정보 삭제
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않고, 모든 요청 방식에 대해 새로운 인증 요구 -> 주로 RESTful API에서 사용
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password(bCryptPasswordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(bCryptPasswordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
