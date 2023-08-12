package com.arkwith.starter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.arkwith.starter.answer.Answer;
import com.arkwith.starter.answer.AnswerRepository;
import com.arkwith.starter.question.Question;
import com.arkwith.starter.question.QuestionRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
class StarterApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void testJpa() {

		// Question question = new Question();
		// question.setSubject("sbb가 무엇인가요?");
		// question.setContent("ssb에 대해 알고 싶습니다.");
		// question.setCreateDate(LocalDateTime.now());

		// this.questionRepository.save(question); // 첫번째 질문 저장

		// Question question2 = new Question();
		// question2.setSubject("스프링부트 모델 질문입니다.");
		// question2.setContent("id는 자동으로 생성되나요?");
		// question2.setCreateDate(LocalDateTime.now());

		// this.questionRepository.save(question2); // 두번째 질문 저장
		
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());
		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());

	}

	@Test
	void testFindBySubject() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testFindBySubjectAndContent() {
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "ssb에 대해 알고 싶습니다.");
		assertEquals("sbb가 무엇인가요?", q.getSubject());
		assertEquals("ssb에 대해 알고 싶습니다.", q.getContent());
		assertEquals(1,q.getId());

	}
	
	@Test
	void testFindBySubjectLike() {
		List<Question> qs = this.questionRepository.findBySubjectLike("sbb%");
		assertEquals(1, qs.size());
		assertEquals("sbb가 무엇인가요?", qs.get(0).getSubject());
	}

	@Test
	void testUpdateQuestion() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());

		Question q = oq.get();
        q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	void testDeleteQuestion() {
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());

	}

	@Test
	void testCreateAnswer() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setCreateDate(LocalDateTime.now());
		a.setQuestion(q);

		this.answerRepository.save(a);
	}

	@Test
	void testFindByIdOfAnswer() {
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Transactional
	@Test
	void testGetAnswerOfQuestion() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	void testCreateDummyDataForQuestion() {
		for (int i = 0; i < 100; i++) {
			Question q = new Question();
			q.setSubject(String.format("테스트 데이터 입니다 : [%03d]", i));
			q.setContent("내용무 : " + i);
			q.setCreateDate(LocalDateTime.now());

			this.questionRepository.save(q);
		}
	}

}
