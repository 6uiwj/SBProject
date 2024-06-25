package com.example.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor //final이 붙은 속성을 포험하는 생성자를 자동으로 만들어 줌 (자동의존주입 - 생성자 방식)
public class QuestionController {

    private final QuestionRepository questionRepository;

    @GetMapping("/question/list")
    public String list(Model model) { //데이터를 저장할 Model 객체를 매개변수로 지정
        /**
         * Model : 자바 객체와 템플릿 간 연결 고리 역할
         *          Model 객체를 따로 생성할 필요 없이 매개변수로 지정하면 스프링 부트가 자동으로 Model 객체 생성
         */
        List<Question> questionList = this.questionRepository.findAll(); //질문 목록을 전부 조회해 questionList에 담음
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

}
