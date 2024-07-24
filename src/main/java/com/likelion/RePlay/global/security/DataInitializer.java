package com.likelion.RePlay.global.security;

import com.likelion.RePlay.domain.user.entity.Role;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.domain.user.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(1L, RoleName.ROLE_USER));
            roleRepository.save(new Role(2L, RoleName.ROLE_ADMIN));
        }
    }
}