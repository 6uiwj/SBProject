package com.example.sbb.question;

import com.example.sbb.answer.AnswerForm;
import com.example.sbb.user.SiteUser;
import com.example.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/question") //프리픽스 설정 (URL 접두사)
@RequiredArgsConstructor //final이 붙은 속성을 포험하는 생성자를 자동으로 만들어 줌 (자동의존주입 - 생성자 방식)
public class QuestionController {

    //private final QuestionRepository questionRepository; //서비스 생성 전
    private final QuestionService questionService;
    private final UserService userService;

    /**
     * 목록 조회 메서드
     * @param model
     * @param page @RequestParam : 웹 요청 파라미터를 컨트롤러 메서드의 매개변수로 바인딩
     *                             주로 http 요청의 쿼리 파라미터나 폼 데이터를 가져오는데 사용
     *                              ex) http://localhost..../question/list?page=0 에서 page 값을 가져 옴
     *                  defaultValue = "0" : url에 매개변수로 page가 전달되지 않은 경우 기본값은 0이 되도록 설정
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue = "0") int page) { //데이터를 저장할 Model 객체를 매개변수로 지정
        /**
         * Model : 자바 객체와 템플릿 간 연결 고리 역할
         *          Model 객체를 따로 생성할 필요 없이 매개변수로 지정하면 스프링 부트가 자동으로 Model 객체 생성
         */
        //List<Question> questionList = this.questionRepository.findAll(); //질문 목록을 전부 조회해 questionList에 담음
        Page<Question> paging = this.questionService.getList(page); //서비스를 통해 질문 목록 조회
        model.addAttribute("paging", paging);
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
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) { //검증 결과에 error가 있으면 다시 작성 화면으로 돌아감
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        //오류가 없으면 등록
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    /**
     * 질문 수정하기 - 수정할 질문 조회
     * @param questionForm
     * @param id
     * @param principal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id")
                                 Integer id, Principal principal) {
        //id로 질문 조회
        Question question = this.questionService.getQuestion(id);
        //현재 로그인한 사용자와 질문의 작성자가 동일하지 않은 경우 오류 발생
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        //제목과 내용을 담아 question_form 템플릿에 전달
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    /**
     * 질문 수정, 저장
     * @param questionForm
     * @param bindingResult
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()") //로그인 상태인지 확인
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult
                                 bindingResult, Principal principal, @PathVariable("id") Integer id) {
        //questionForm 커맨드 객체 검증 결과에 에러가 있다면 다시 폼 반환
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        //id로 질문 조회
        Question question = this.questionService.getQuestion(id);
        //글쓴이와 로그인한 사용자가 같지 않다면 에러
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");

        }
        //일치한다면 서비스의 modify 메서드로 내용 수정
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        //수정 완료 후 리다이렉트 할 페이지
        return String.format("redirect:/question/detail/%s", id);
    }

    /**
     * 게시글 삭제
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id); //id로 게시글 조회
        //게시글 작성자가 로그인 사용자와 다를 경우 에러
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        //게시글 작성자가 로그인 사용자와 동일할 경우 삭제
        this.questionService.delete(question);
        //삭제 후 질문 목록 화면으로 이동
        return "redirect:/";
    }

    /**
     * [추천]버튼을 눌렀을 때 GET 방식으로 호출
     * @param principal
     * @param id
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question =this.questionService.getQuestion(id); //id로 질문 조회
        SiteUser siteUser = this.userService.getUser(principal.getName()); //사용자 아이디를 가져와 siteUser객체에 담음
        this.questionService.vote(question,siteUser); //추천인에 저장
        return String.format("redirect:/question/detail/%s", id); //질문 상세화면으로 리다이렉트
    }

}
