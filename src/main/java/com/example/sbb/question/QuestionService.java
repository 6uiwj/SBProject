package com.example.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor //생성자 방식 의존주입
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    /**
     * 질문 목록 데이터 조회
     * @return
     */
    public List<Question> getList() {
        return this.questionRepository.findAll();
    }
}
