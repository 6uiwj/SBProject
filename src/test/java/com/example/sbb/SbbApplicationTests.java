package com.example.sbb;

import com.example.sbb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService; //3-02 테스트데이터 만들기

	/*
	@Autowired //질문 엔티티생성 시 questionRepository가 필요하므로 의존주입
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;
	 */

	//@Transactional
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

		/*
		//findBySubjectAndContent 메서드
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1,q.getId());

		 */

		/*
		//findBySubjectLike 메서드
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0); //List 중 0번째 데이터를 가져옴
		assertEquals("sbb가 무엇인가요?",q.getSubject());


		 */

		/*
		//그만 좀 해 제바ㅏ아아알
		Optional<Question> oq = this.questionRepository.findById(1); //id가 1인 데이터 가져오기
		assertTrue(oq.isPresent()); //데이터가 존재하는 지 확인 (존재하지 않으면 오류발생)
		//oq가 존재함을 확인 하고, get으로 가져온 다음, Question 객체로 변환해서 담음
		Question q = oq.get();
		q.setSubject("수정된 제목"); //제목 변경
		this.questionRepository.save(q); //저장

		 */

		/*
		//질문 삭제하기
		//questionRepository.count() : 테이블 행의 개수 리턴
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1); //Id가 1인 데이터 반환
		assertTrue(oq.isPresent()); //존재여부 확인
		Question q = oq.get(); //Question 객체로 변경
		this.questionRepository.delete(q); //데이터 삭제
		assertEquals(1, this.questionRepository.count()); //삭제 후 테이블에 행이 1개인지 확인

		 */

		/*
		//답변 생성하기
		//답변 생성을 위해 질문 데이터를 먼저 가져옴
		Optional<Question> oq = this.questionRepository.findById(2); //ID가 2인 데이터 가져옴
		assertTrue(oq.isPresent()); //데이터가 존재하는지 확인
		Question q = oq.get(); //존재하면 Question 객체로 변환

		Answer a = new Answer(); //Answer 객체 생성
		a.setContent("네 자동으로 생성됩니다."); //답변 등록
		a.setQuestion(q); //답변 엔티티의 question 속성에 질문 데이터를 대입해 답변 데이터 생성
		a.setCreateDate(LocalDateTime.now()); //답변 시간 저장
		this.answerRepository.save(a); //저장


		 */

		/*
		//답변 데이터 조회하기
		Optional<Answer> oa = this.answerRepository.findById(1); //Id가 1인 답변 데이터 조회
		assertTrue(oa.isPresent()); //존재하는지?
		Answer a = oa.get(); //Answer 객체고 변환
		assertEquals(2, a.getQuestion().getId()); //이 답변이 달린 질문 데이터의 Id가 2인지 확인

		 */

		/*
		//답변 데이터를 통해 질문 데이터 찾기 vs 질문 데이터를 통해 답변 데이터 찾기
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1,answerList.size());
		assertEquals("네 자동으로 생성됩니다." , answerList.get(0).getContent());


		 */

		/**
		 * 3-02 테스트 데이터 만들기
		 */
		for (int i = 1; i <= 300; i++) {
			//[%03d]: 제목에 번호를 부여 -> 세 자리 포맷, 빈 자리에 왼쪽부터 0을 채움 (001, 013, 213...)
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용 없음 ";
			this.questionService.create(subject, content, null);
		}

	}
}
