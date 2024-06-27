package com.example.sbb.question;

import com.example.sbb.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public void create(String subject, String content) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q);
    }

}
