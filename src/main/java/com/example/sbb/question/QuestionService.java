package com.example.sbb.question;

import com.example.sbb.DataNotFoundException;
import com.example.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor //생성자 방식 의존주입
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;


    /**
     * 질문 목록 데이터 조회 - 페이징 처리
     * 페이지 번호를 입력받아 해당 페이지의 Page 객체를 리턴
     * @param page : 조회할 페이지 번호
     * @return 해당 페이지 데이터 조회(페이지 객체 리턴)
     */
    public Page<Question> getList(int page) {
        //게시물 최신순 정렬
        //Sort.Order: 정렬 기준을 정의하는 Spring Data JPA의 클래스
        List<Sort.Order> sorts = new ArrayList<>();
        //정렬 기준 추가 - "createDate" 필드 기준 내림차순 정렬
        sorts.add(Sort.Order.desc("createDate"));
        //인수
        // page: 조회할 페이지의 번호, 10: 한 페이지에 보여줄 게시물의 수
        //Sort.by(sorts)) 위에서 정의해준 정렬방법
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //한 페이지 당 10개 게시물 출력
        return this.questionRepository.findAll(pageable);
    }

    /**
     * id 값으로 질문 데이터 조회
     * @param id
     * @return
     */
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        //Optional 객체이므로 데이터 존재여부 확인
        if (question.isPresent()) {
            return question.get();
        } else {
            //데이터가 없을 경우 예외 실행
            throw new DataNotFoundException("question not found");
        }
    }

    /**
     * 질문 데이터 저장하기
     * @param subject
     * @param content
     */
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

}
