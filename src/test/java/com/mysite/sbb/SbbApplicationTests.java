package com.mysite.sbb;

import com.mysite.sbb.meetingBoard.service.AnswerService;
import com.mysite.sbb.meetingBoard.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Transactional
@SpringBootTest
class SbbApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private QuestionService questionService;

	@Autowired
	private AnswerService answerService;

//	@Test
//	void testJpa() {
//		for (int i = 1; i <= 300; i++) {
//			String subject = String.format("테스트 데이터입니다:[%03d]", i);
//			String content = "내용무";
//			this.questionService.create(subject, content, null);
//		}
//	}
//	@Test
//	void testJpa1() {
//		for (int i = 1; i <= 15; i++) {
//			Question question = questionService.getQuestion(300l);
//			String content = "내용무";
//			SiteUser author = question.getAuthor();
//			this.answerService.create(question, content, author);
//		}
//	}

}
