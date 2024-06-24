package com.example.sbb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SbbApplicationTests {

	@Autowired //질문 엔티티생성 시 questionRepository가 필요하므로 의존주입
	private QuestionRepository questionRepository;

	@Test
	void testJpa() {
		/*
		Question q1 = new Question(); //Question엔티티의 q1 객체 생성
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1); //리포지토리를 통해 저장

		Question q2 = new Question(); //Quesiton엔티티의 q2 객체 생성
		q2.setSubject("스프링 부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2); //리포지토리를 통해 저장

		 */

		/*
		//findAll() = SELECT * FROM QUESTION
		List<Question> all = this.questionRepository.findAll();
		//assertEquals(기댓값, 실젯값) = 테스트에서 예상한 결과와 실제 결과가 동일한지 확인
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());

		 */


		/*
		//findById : Id를 통해 조회
		//null값에 대해 유연하게 처리하기 위해 Optional 사용
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()) { //isPresent: 값의 존재 확인
			Question q = oq.get(); //존재하면 객체 값 가져오기
			assertEquals("sbb가 무엇인가요?", q.getSubject());

		 */

		/*
		//findBySubject
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());

		 */

		//findBySubjectAndContent 메서드
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1,q.getId());

		}

}
