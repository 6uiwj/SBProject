package com.example.sbb.answer;

import com.example.sbb.DataNotFoundException;
import com.example.sbb.question.Question;
import com.example.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

//답변을 등록하는 서비스
@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;


    /**
     * 답변 생성  - 답변이 등록된 위치로 이동하기 위해(앵커 기능) Answer 객체 리턴
     * @param question
     * @param content
     * @param author
     * @return
     */
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer(); //Answer 객체 생성
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer); //저장
        return answer;
    }

    /**
     * 답변 id로 조회 - 존재하면 값을 가져오고, 존재하지 않으면 에러 송출
     * @param id
     * @return
     */
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    /**
     * 수정 메서드 -> 답변내용과 수정시간 변경 후 저장
     * @param answer
     * @param content
     */
    public void modify(Answer answer, String content ) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    /**
     * 답변 삭제
     * @param answer
     */
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    /**
     * 추천한 사람 저장
     * @param answer
     * @param siteUser
     */
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser); //siteUser를 가져와 answer엔티티의 voter에 담음
        this.answerRepository.save(answer);
    }
}
