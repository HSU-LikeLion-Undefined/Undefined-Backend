package com.likelion.RePlay.global.security;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneId) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByPhoneId(phoneId)
                .orElseThrow(()-> new UsernameNotFoundException("찾을 수 없는 전화번호입니다."));
    }
}
