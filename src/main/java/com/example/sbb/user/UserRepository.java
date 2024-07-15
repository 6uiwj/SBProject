package com.example.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    //ID로 SiteUser 엔티티 조회
    Optional<SiteUser> findByUsername(String username);
}
