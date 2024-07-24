package com.likelion.RePlay.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationUserUtils {
    Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    public String getCurrentUserId() {
        System.out.println((String)authentication.getPrincipal());
        return (String) authentication.getPrincipal();
    }
}
