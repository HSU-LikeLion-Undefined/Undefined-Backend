package com.likelion.RePlay.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 비로그인 사용자도 접근 가능
                        .requestMatchers("/api/user/signUp", "/api/user/login",
                                "/api/user/isExistNickName","/api/user/isExistPhoneId", "/api/user/getMyPage").permitAll()
                        .requestMatchers("/api/playing/getPlayings/**").permitAll()
                        .requestMatchers("/api/info/getAllInfo", "/api/info/getOneInfo/**").permitAll()
                        .requestMatchers(("/api/learning/getLearning/**")).permitAll()

                        // 로그인한 사용자만 접근 가능
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/playing/**").authenticated()
                        .requestMatchers("/api/info/submitInfo").authenticated()
                        .requestMatchers("/api/info/**").authenticated()
                        .requestMatchers("/api/learning/**").authenticated()

                        //  관리자만 접근 가능
                        .anyRequest().hasAnyAuthority("ROLE_ADMIN")
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}