package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    /**
     * 답변 생성
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

    /**
     * 답변 수정 메서드 - ID로 답변 조회,
     * @param answerForm
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) { //로그인 사용자와 답변 작성자가 일치하지 않을 때
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다."); //에러 출력
        }
        answerForm.setContent(answer.getContent()); //조회한 답변 데이터의 내용읗 AnswerForm 객체에 대입,
        return "answer_form";
    }

    /**
     * 답변 수정 메서드(post)
     * @param answerForm
     * @param bindingResult
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        //AnswerForm 커맨드 객체 검증 결과에 에러가 있다면 다시 폼 반환
        if( bindingResult.hasErrors()) {
            return "answer_form";
        }
        //id로 답변 조회
        Answer answer = this.answerService.getAnswer(id);
        //글쓴이와 로그인한 사용자가 같지 않다면 에러
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        //일치한다면 서비스의 modify 메서드로 내용 수정
        this.answerService.modify(answer, answerForm.getContent());
        //수정 완료 후 리다이렉트 할 페이지
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    /**
     * 답변 삭제
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");

        }
        this.answerService.delete(answer);
        //삭제 후 질문 상세 화면으로 이동
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());

    }

    /**
     * 답변 추천한 사람 저장
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id); //id로 답변 조회
        SiteUser siteUser = this.userService.getUser(principal.getName()); //로그인한 사용자를 사이트 유저목록에서 조회하여 siteUser 객체에 담음
        this.answerService.vote(answer, siteUser); //추천한 사람 저장
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }
}
