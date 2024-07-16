package com.example.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //스프링의 환경설정 파일임을 의미
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 설정(스프링 시큐리티 활성화)
@EnableMethodSecurity(prePostEnabled = true) //@PreAuthorize를 사용하기 위해 필요한 설정
public class SecurityConfig {
    //스프링 시큐리티 인증기능 무력화
    //SecurityFilterChain 클래스 : 모든 요청 URL에 필터로 적용되어 URL 별로 특별한 설정을 할 수 있게 해줌
    @Bean //SecurityFilterChain 빈을 생성하여 스프링 시큐리티 세부 설정
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //인증되지 않은 모든 페이지의 요청을 허락 (로그인하지 않아도 페이지 접근가능)
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf((csrf) -> csrf //csrf 처리 시 h2 콘솔은 예외처리
                        .ignoringRequestMatchers(new AntPathRequestMatcher
                                ("/h2-console/**")))
                .headers((headers) -> headers //H2 콘솔 화면 깨짐 처리
                        .addHeaderWriter(new XFrameOptionsHeaderWriter( //URL 요청 시 X-Frame-Options 헤더를 DENY 대신 SAMEORIGIN으로 설정
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN // -> 프레임에 포함된 웹 페이지가 동일한 사이트에서 제공할 때에만 사용 허락
                        )))
                .formLogin((formLogin) -> formLogin //로그인 URL, 로그인 성공시 URL 설정
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/"))
                .logout((logout) -> logout //로그아웃 URL, 로그아웃 성공시 URL 설정, 로그아웃시 사용자 세션 삭제
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));
        return http.build();
    }

    //BCryptEncoder 빈 만들기
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager : 스프링 시큐리티의 인증을 처리
     *     사용자 인증 시 UserSecurityService와 PasswordEncoder를 내부적으로 사용하여
     *      인증과 권한 부여 프로세스 처리
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
