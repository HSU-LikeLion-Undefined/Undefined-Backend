package com.likelion.RePlay.domain.user.repository;

import com.likelion.RePlay.domain.user.entity.Role;
import com.likelion.RePlay.global.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}