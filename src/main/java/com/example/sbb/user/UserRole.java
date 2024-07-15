package com.example.sbb.user;

import lombok.Getter;

@Getter //값 변경이 필요없으므로 setter는 부여X
public enum UserRole {
    ADMIN("ROLE_ADMIN"), //상수(값)
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
