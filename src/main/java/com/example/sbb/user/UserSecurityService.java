package com.example.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
UserDetailsService 인터페이스(스프링시큐리티 제공)
  - loadUserByUsername 메서드를 강제로 구현하도록 되어있음
    - loadUserByUsername 메서드: 사용쟈명(username)으로 스프링 시큐리티 사용자(User) 객체를 조회하여 리턴하는 메서드

 */
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService { //인터페이스 구현 필수

    private final UserRepository userRepository;

    /**
     * 사용자명(username)으로 SiteUser 객체 조회
     *  만약 사용자 명에 해당하는 데이터가 없을 경우 UsernameNotFoundException 발생시킴
     *  사용자 명이 'admin'인 경우 ADMIN 권한
     *  그 외의 경우 USER 권한 부여
     * @param username the username identifying the user whose data is required.
     * @return User 객체 반환 (User 생성자에 사용자명, 비밀번호, 권한 리스트 전달)
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username); //username으로 SiteUser 객체 조회
        if (_siteUser.isEmpty()) { //조회 정보가 없을 때
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        SiteUser siteUser = _siteUser.get(); //조회 정보가 있을 때 SiteUser 정보를 가져온다.

        //사용자의 권한 정보를 나타내는 GrantedAuthority 객체를 생성하는 데 사용할 리스트 생성
        List<GrantedAuthority> authorities = new ArrayList<>(); //

        if("admin".equals(username)) { //username이 admin이면 ADMIN 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else { //그 외에는 USER 권한 부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }

}
