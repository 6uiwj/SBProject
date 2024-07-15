package com.example.sbb.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser { //회원 엔티티

    @Id //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동으로 고유 키값 생성
    private Long id;

    @Column(unique = true) //유일성 보장
    private String username;

    private String password;

    @Column(unique = true) //유일성 보장
    private String email;
}
