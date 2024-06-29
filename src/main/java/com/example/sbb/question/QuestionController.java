package com.example.sbb.question;

import com.example.sbb.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/question") //프리픽스 설정 (URL 접두사)
@RequiredArgsConstructor //final이 붙은 속성을 포험하는 생성자를 자동으로 만들어 줌 (자동의존주입 - 생성자 방식)
public class QuestionController {

    //private final QuestionRepository questionRepository; //서비스 생성 전
    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model) { //데이터를 저장할 Model 객체를 매개변수로 지정
        /**
         * Model : 자바 객체와 템플릿 간 연결 고리 역할
         *          Model 객체를 따로 생성할 필요 없이 매개변수로 지정하면 스프링 부트가 자동으로 Model 객체 생성
         */
        //List<Question> questionList = this.questionRepository.findAll(); //질문 목록을 전부 조회해 questionList에 담음
        List<Question> questionList = this.questionService.getList(); //서비스를 통해 질문 목록 조회
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    /**
     * id에 해당하는 질문의 상세페이지 템플릿 가져오기
     * @param model
     * @param id
     * @return
     */
    //@PathVariable : 경로변수? URL의 {id}를 변수로 값을 주기 위해서
    // @PathVariable("A")와 @GetMapping(value = "/../../{A}")에서 A의 이름이 동일해야 함
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        //Service를 통해 지정한 id에 해당하는 질문을 가져온다.
        Question question = this.questionService.getQuestion(id);
        //model 객체에 question이라는 이름으로 조회한 질문 저장!
        model.addAttribute("question", question);
        return "question_detail";
    }

    /**
     * 질문 등록 URL 템플릿 연결
     * @param questionForm 템플릿에 th:object="${questionForm}"을 추가했으므로 매개변수 필요
     * @return
     */
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }


    /**
     * 질문 등록(저장) 메서드 -> 메서드 오버로딩
     * @param subject (화면에서 입력한 제목)
     * @param content (화면에서 입력한 내용)
     *                -> question_form.html에서 입력한 subject, cntent의 내용과
     *                ReauestParam의 value값이 동일해야 함
     * @return
     */
    /*
    @PostMapping("/create")
    public String questionCreate(@RequestParam(value="subject") String subject,
                                 @RequestParam(value="content") String content) {
        this.questionService.create(subject,content);
        return "redirect:/question/list"; //질문 저장 후 질문 목록으로 이동
    }
    */

    /**
     * 질문 등록(저장) 메서드 -> 메서드 오버로딩
     * @param questionForm subject, content를 매개변수로 직접 받는 것이 아닌 Form으로 바인딩
     *                     폼의 바인딩 기능: subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이
     *                     자동으로 바인딩(이름이 동일하면 함께 연결되어 묶임)
     * @param bindingResult @Valid 애너테이션으로 검증이 수행된 결과를 의미하는 객체(항상 @Valid 매개변수 뒤에 위치)
     * @return
     */
    //@Valid : QuestionForm의 검증 기능 동작(@Size @NotEmpty 등..)
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //검증 결과에 error가 있으면 다시 작성 화면으로 돌아감
            return "question_form";
        }
        //오류가 없으면 등록
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }

}
