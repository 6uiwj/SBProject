package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    /**
     *
     * @param model
     * @param id @PathVariable 경로를 변수로 지정 "id"를 {id}에 대입
     * @param answerForm
     * @param bindingResult
     * @param principal 현재 로그인한 사용자의 정보를 알려줌
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}") // /answer/create/{id} URL 요청 시 createAnswer 메서드가 호출되도록 post 매핑
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
                               BindingResult bindingResult, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName()); //유저 ID 가져오기
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        this.answerService.create(question, answerForm.getContent(), siteUser); //답변 등록 서비스 추가
        return String.format("redirect:/question/detail/%s",id);
    }
}
