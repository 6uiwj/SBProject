package com.example.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    //@PathVariable : 경로변수? URL의 {id}를 변수로 값을 주기 위해서
    // @PathVariable("A")와 @GetMapping(value = "/../../{A}")에서 A의 이름이 동일해야 함
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        //Service를 통해 지정한 id에 해당하는 질문을 가져온다.
        Question question = this.questionService.getQuestion(id);
        //model 객체에 question이라는 이름으로 조회한 질문 저장!
        model.addAttribute("question", question);
        return "question_detail";
    }

}
