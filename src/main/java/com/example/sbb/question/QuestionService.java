package com.example.sbb.question;

import com.example.sbb.DataNotFoundException;
import com.example.sbb.answer.Answer;
import com.example.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
     * 검색어 kw 를 입력받아 쿼리의 조인문과 where문을 Specification 객체로 생성하여 리턴
     * @param kw - 검색어
     * @return
     */
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            /**
             * 검색 기능 구현
             * @param q must not be {@literal null}. : Root 자료형,
             *          기준이 되는 Question 엔티티 객체 의미,
             *          질문 제목과 내용 검색을 위해 필요
             * @param query must not be {@literal null}.
             * @param cb must not be {@literal null}.
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?>
                query, CriteriaBuilder cb) {
                query.distinct(true); //중복 제거


                //u1: Question 엔티티와 SiteUser 엔티티를 아우터 조인하여 만든 SiteUser엔티티의 객체
                //    QUestion 엔티티와 SiteUser 엔티티는 author 속성을 연결되어 있어서 q.join("author")과 같이 조인
                //     -> 질문 작성자 검색을 위해 필요
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);


                //a : Question 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체
                //    Question 엔티티와 Answer 엔티티는 ansewrList 속성으로 연결되어 있어서 q.join("answerList")로 조인
                //      -> 답변 내용 검색 시 필요
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);

                //u2 : a객체와 SiteUser 엔티티를 아우터 조인 하여 만든 SIteUser 엔티티의 객체
                //      -> 답변 작성자를 검색할 때 필요
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

                //각각 like 키워드로 검색후 or로 하나라도 만족하는 경우 반환
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), //제목
                        cb.like(q.get("content"), "%" + kw + "%"), //내용
                        cb.like(u1.get("username"), "%" + kw + "%"), //질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"), //답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%")); //답변 작성자
            }
        };
    }

    /**
     * 질문 목록 데이터 조회 - 페이징 처리
     * 페이지 번호를 입력받아 해당 페이지의 Page 객체를 리턴
     * @param page : 조회할 페이지 번호
     * @param kw : 검색 키워드
     * @return 해당 페이지 데이터 조회(페이지 객체 리턴)
     */
    public Page<Question> getList(int page, String kw) {
        //게시물 최신순 정렬
        //Sort.Order: 정렬 기준을 정의하는 Spring Data JPA의 클래스
        List<Sort.Order> sorts = new ArrayList<>();
        //정렬 기준 추가 - "createDate" 필드 기준 내림차순 정렬
        sorts.add(Sort.Order.desc("createDate"));
        //인수
        // page: 조회할 페이지의 번호, 10: 한 페이지에 보여줄 게시물의 수
        //Sort.by(sorts)) 위에서 정의해준 정렬방법
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); //한 페이지 당 10개 게시물 출력
        //Specification<Question> spec = search(kw);
        //return this.questionRepository.findAll(spec, pageable);
        //@Query를 사용하여 검색기능 구현
        return this.questionRepository.findAllByKeyword(kw, pageable);
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

    /**
     * 질문 수정
     * @param question
     * @param subject
     * @param content
     */
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    /**
     * 게시글 삭제
     * @param question
     */
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    /**
     * 추천 기능
     * @param question
     * @param siteUser
     */
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }
}
