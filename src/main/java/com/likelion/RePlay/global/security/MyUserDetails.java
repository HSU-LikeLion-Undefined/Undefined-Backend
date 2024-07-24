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
    public UserDetails loadUserByUsername(String phoneId) throws UsernameNotFoundException {
       User user =userRepository.findByPhoneId(phoneId).orElseThrow(RuntimeException::new);

       List<GrantedAuthority> authorities = user.getUserRoles().stream()
               .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName().name()))
               .collect(Collectors.toUnmodifiableList());

       return new org.springframework.security.core.userdetails.User(
               user.getPhoneId(), user.getPassword(), authorities);
    }
}
