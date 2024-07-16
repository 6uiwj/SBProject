package com.example.sbb.answer;

import com.example.sbb.question.Question;
import com.example.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    //부모 자식 관계에서 ManyToOne 사용 (부모 Question, 자식 Answer)
    @ManyToOne //질문 엔티티와 연결되었다는 표시 (외래키 관계, 하나의 질문에 여러개의 답변이 달릴 수있으므로 Many to One = N:1관계)
    private Question question; //질문 엔티티 참조

    @ManyToOne //한 사람이 여러 개 답변 작성 가능
    private SiteUser author;
}
