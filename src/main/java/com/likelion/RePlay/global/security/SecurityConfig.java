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
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/.well-known/**").permitAll()  // Let's Encrypt 검증 경로 허용
                        .requestMatchers("/api/user/signUp", "/api/user/login",
                                "/api/user/isExistNickName","/api/user/isExistPhoneId", "/api/user/getMyPage").permitAll()
                        .requestMatchers("/api/playing/getPlayings/**", "/api/playing/comment/**",
                                "/api/playing/filtering", "/api/playing/getUserReview").permitAll()
                        .requestMatchers("/api/info/getAllInfo", "/api/info/getOneInfo/**").permitAll()
                        .requestMatchers("/api/learning/getLearnings/**", "/api/learning/comment/**",
                                "/api/learning/filtering", "/api/learning/getMentorReview").permitAll()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/playing/**").authenticated()
                        .requestMatchers("/api/info/submitInfo").authenticated()
                        .requestMatchers("/api/info/**").authenticated()
                        .requestMatchers("/api/learning/**").authenticated()
                        .anyRequest().hasAnyAuthority("ROLE_ADMIN")
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .requiresChannel(channel ->
                channel.anyRequest().requiresSecure()); // HTTPS로 리디렉션


        return http.build();
    }
}