package com.example.sbb.user;

import com.example.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 데이터 생성 메서드
     * @param useranme
     * @param email
     * @param password BCrypt를 이용해 암호화하여 저장
     * @return user 엔티티에 저장
     */
    public SiteUser create(String useranme, String email, String password) {
        SiteUser user = new SiteUser(); //엔티티 객체 생성
        user.setUsername(useranme); //필드에 데이터 저장
        user.setEmail(email);

        /*
        //비크립트 해시 함수를 사용
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));
         */
        //빈으로 등록한 PasswordEncoder 객체를 주입받아 사용
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user); //리포지토리를 통해 저장
        return user;
    }

    /**
     * 회원(SiteUser) 조회
     * @param username
     * @return
     */
    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if(siteUser.isPresent()) { //siteUser에 데이터가 있으면 가져온다.
            return siteUser.get();
        } else
        throw new DataNotFoundException("siteuser not found"); //없으면 예외 발생
    }
}
