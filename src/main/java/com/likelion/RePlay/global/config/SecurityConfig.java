package com.likelion.RePlay.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    // SecurityFilterChain 커스텀 빈 스프링 빈으로 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF 보호를 비활성화 (CSRF : 악의적인 웹 사이트가 사용자를 대신해 요청을 보내는 공격) -> RESTful API 서버에서 필요 X
        http
                .csrf(AbstractHttpConfigurer::disable)
                //HTTP 기본 인증을 비활성화합 (기본 인증은 브라우저의 기본 로그인 창을 사용하는 방식) -> 사용자 친화적인 로그인 폼 사용을 위해
                .httpBasic(AbstractHttpConfigurer::disable)
                //기본 로그인 폼을 비활성화 -> 커스텀 로그인 폼 사용
                .formLogin(AbstractHttpConfigurer::disable)
                //URL 경로에 따른 접근 권한 설정
                .authorizeHttpRequests((authorize) -> authorize
                        //"/signup", "/", "/login" 경로는 누구나 접근할 수 있도록 -> 그외의 경로는 인증된 사용자만 접근
                        .requestMatchers("/signup", "/", "/login").permitAll()
                        // "/admin"은 ADMIN 권한을 가진 경우에만 허용
                        .requestMatchers("/admin").hasRole("ADMIN")
                        //그 외의 모든 요청은 인증된 사용자만 접근
                        .anyRequest().authenticated())
                .logout((logout) -> logout
                        //로그아웃 후 이동할 페이지를 /login으로 설정
                        .logoutSuccessUrl("/login")
                        //로그아웃 시 세션 무효화 -> 사용자의 세션 정보 삭제
                        .invalidateHttpSession(true))
                //세션 관리를 설정
                .sessionManagement(session -> session
                        //세션을 사용하지 않고, 모든 요청 방식에 대해 새로운 인증 요구 -> 주로 RESTful API에서 사용
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
