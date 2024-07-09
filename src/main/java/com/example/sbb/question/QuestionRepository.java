package com.example.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JpaRepository<엔티티, 기본키>
 *  - JPA가 제공하는 인터페이스
 *  - CRUD작업을 처리하는 메서드 내장
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    //'findBy + 엔티티의 속성명'으로 메서드를 작성하면 입력한 속성의 값으로 데이터 조회 가능

    //SELECT * FROM QUESTION WHERE SUBJECT = 조건값
    Question findBySubject(String subject);

    //SELECT * FROM QUESTION WHERE SUBJECT = 조건값 AND WHERE CONTENT = 조건값
    Question findBySubjectAndContent(String subject, String content);

    //결과가 여러 건인 경우 리턴타입을 List로 작성
    //ex: List<Question> findBy.....(   , ..  )..

    //SELECT * FROM QUESTION WHERE SUBJECT LIKE '~';
    List<Question> findBySubjectLike(String subject);

    //페이징 구현
    Page<Question> findAll(Pageable pageable);
}
