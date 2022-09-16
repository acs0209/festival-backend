package com.mysite.sbb.meetingBoard.service;


import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.answer.AnswerRepository;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.exception.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    // 답변 생성
    public void create(Question question, String content, String username, String password) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setUsername(username);
        answer.setPassword(password);
        this.answerRepository.save(answer);
    }

    // 답변 페이징 처리
    public Page<Answer> getList(int page, Long id) {
        Question question = questionService.getQuestion(id);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.answerRepository.findAllByQuestion(question, pageable);
    }

    // 아래 두 함수는 AnswerController 에서 필요한 답변조회와 답변수정 기능
    public Answer getAnswer(Long id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("요청하신 데이터를 찾을 수 없습니다.");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        this.answerRepository.save(answer);
    }

    public Boolean delete(Answer answer) {
        this.answerRepository.delete(answer);;
        return true;
    }
}
