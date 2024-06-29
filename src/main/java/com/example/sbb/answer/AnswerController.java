package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    /**
     * @RequestParam : 쿼리 파라미터나 폼 데이터를 메서드의 매개변수에 바인딩할 때 사용
     *      템플릿에서 입력한 내용을 얻기 위해
     * @RequestParam(value="content")
     *      :question_detail.html에서 name="content"인 text에 입력된 내용을 얻기 위해 사용
     */
    @PostMapping("/create/{id}") // /answer/create/{id} URL 요청 시 createAnswer 메서드가 호출되도록 post 매핑
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
                               BindingResult bindingResult) {
        Question question = this.questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }

        this.answerService.create(question, answerForm.getContent()); //답변 등록 서비스 추가
        return String.format("redirect:/question/detail/%s",id);
    }
}
