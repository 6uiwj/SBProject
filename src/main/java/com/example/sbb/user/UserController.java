package com.example.sbb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 템플릿 연결 (/user/signup)
     * @param userCreateForm
     * @return
     */
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    /**
     * 회원가입 진행
     * @param userCreateForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        //비밀번호와 비밀번호 확인이 일치하는지 검증, 불일치 시에는 2개의 비밀번호가 일치하지 않는다는 오류 발생
        //bindingResult.rejectValue(필드명, 오류 코드, 오류 메시지)
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch(DataIntegrityViolationException e) { //중복된 데이터에 대한 예외를 처리하는 코드
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) { //중복 이외의 예외를 처리하는 코드
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage()); //구체적 오류 메시지 출력
            return "signup_form";
        }
        //서비스를 통해 저장
        return "redirect:/";
    }

    /**
     * 로그인 URL 매핑, login_form 템플릿 출력
     * 실제 로그인 과정(PostMapping)은 스프링 시큐리티가 대신 처리하므로 우리가 직접 구현할 필요 없다.
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login_form";
    }
}
