package com.mysite.sbb;

import com.mysite.sbb.meetingBoard.meetingService.AnswerService;
import com.mysite.sbb.meetingBoard.meetingService.QuestionService;
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



}
