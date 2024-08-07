package com.example.sbb.question;

import com.example.sbb.answer.Answer;
import com.example.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity //스프링 부트가 Question 클래스를 엔티티로 인식
@Getter
@Setter //원래 엔티티에는 setter 사용X (함부로 변경할 수 없게(무결성))
public class Question {
    @Id //기본키 지정 (중복 불가)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증감 번호 설정(고유 번호 생성)
    private Integer id;

    @Column(length=200)
    private String subject;

    @Column(columnDefinition = "TEXT") //텍스트를 열 데이터로 넣을 수 있게 해줌(글자 수를 제한할 수 없는 경우 사용)
    private String content;

    private LocalDateTime createDate;

    //Cascade(영속성 전이) : 객체 간의 연관관계에서 한 객체의 상태 변경이 다른 객체에 영향을 미치도록 하는 기능
    //CascadeType.REMOVE: 부모 테이블의 행이 삭제되면 자동으로 관련 자식 테이블의 행도 삭제되는 옵션
    //mappedBy : 주로 양방향 관계에서 주인 엔티티에 씀
    //  -> 참조 엔티티의 속성명 (Answer엔티티의 question 필드)
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList; //Answer 객체들

    @ManyToOne //한명이 여러 개 질문 작성 가능
    private SiteUser author;

    private LocalDateTime modifyDate; //수정 일시

    //하나의 질문에 여러 사람 추천 가능, 한 사람이 여러 개 질문에 추천 가능
    //voter 속성 값이 서로 중복되지 않도록 Set 자료형 선택
    @ManyToMany
    Set<SiteUser> voter; //추천인
}
