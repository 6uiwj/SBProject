package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final QuestionService questionService;

    /**
     * @RequestParam : 쿼리 파라미터나 폼 데이터를 메서드의 매개변수에 바인딩할 때 사용
     * 템플릿에서 입력한 내용을 얻기 위해
     * name="content"
     */

    @PostMapping("/create/{id}") // /answer/create/{id} URL 요청 시 createAnswer 메서드가 호출되도록 post 매핑
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @RequestParam(value="content") String content) {
        Question question = this.questionService.getQuestion(id);
        //TODO : 답변을 저장한다.
        return String.format("redirect:/question/detail/%s",id);
    }
}
