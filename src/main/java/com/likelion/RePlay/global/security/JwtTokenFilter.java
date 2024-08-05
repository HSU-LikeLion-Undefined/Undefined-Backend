package com.likelion.RePlay.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = jwtTokenProvider.resolveToken(request);
        System.out.println("token : " + token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            jwtTokenProvider.setSecurityContext(token);
            System.out.println("SecurityContext set for user: " + SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            System.out.println("Invalid or missing token");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}