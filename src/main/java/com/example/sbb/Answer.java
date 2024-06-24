package com.example.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

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
}
