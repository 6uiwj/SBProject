package com.example.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    //페이징 구현 - Pageable 객체를 입력받아 Page<Question> 타입 객체 리턴
    Page<Question> findAll(Pageable pageable);

    //검색기능 구현
    //Specification과 Pageable 객체를 사용하여 DB에 Question 엔티티 조회 결과를 페이징하여 반환
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    //@Query 애너테이션을 사용하여 검색 기능 구현
    //  -> 테이블 기준이 아닌 엔티티 기준으로 작성 (site_user가 아닌 SiteUser(엔티티명) 사용)
    //      컬럼명 대신 속성명 사용 (q.author_id=u1.id가 아닌 q.author=u1 사용)
    //@Query에 매개변수로 전달할 kw 문자열은 메서드의 매개변수에 @Param("kw")처럼 사용
    @Query("select "
           + "distinct q "
           + "from Question q "
           + "left outer join SiteUser u1 on q.author=u1 "
           + "left outer join Answer a on a.question=q "
           + "left outer join SiteUser u2 on a.author=u2 "
           + "where "
           + "  q.subject like %:kw% "
           + "  or q.content like %:kw% "
           + "  or u1.username like %:kw% "
           + "  or a.content like %:kw% "
           + "  or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
