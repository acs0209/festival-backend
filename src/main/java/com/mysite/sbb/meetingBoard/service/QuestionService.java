package com.mysite.sbb.meetingBoard.service;

import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.question.QuestionRepository;
import com.mysite.sbb.exception.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;


    public Page<Question> findAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    /*
    *   질문 목록을 조회하여 리턴하는 getList 메서드
    * */
    // 검색어를 의미하는 매개변수 kw를 getList 에 추가하고 kw 값으로 Specification 객체를 생성하여 findAll 메서드 호출시 전달
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }


    /*
    * id 값으로 Question 데이터를 조회하는 getQuestion 메서드
    * 리포지터리로 얻은 Question 객체는 Optional 객체이기 때문에 isPresent 메서드로 해당 데이터가 존재하는지 검사하는 로직이 필요
    * 만약 id 값에 해당하는 Question 데이터가 없을 경우에는 DataNotFoundException 을 발생시키도록 함
    * */
    public Question getQuestion(Long id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("요청하신 데이터를 찾을 수 없습니다.");
        }


    /* id 값으로 데이터를 조회하기 위해서는 리포지터리의 findById 메서드를 사용해야 한다.
       하지만 findById의 리턴 타입은 Question 이 아닌 Optional 임에 주의하자.
       Optional 은 null 처리를 유연하게 처리하기 위해 사용하는 클래스로 위와 같이 isPresent 로 null 이
       아닌지를 확인한 후에 get() 으로 실제 Question 객체 값을 얻어야 한다.
    * */
    }
    public void create(String subject, String content, String username, String password) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setUsername(username);
        q.setPassword(password);
        this.questionRepository.save(q);
        //return q;
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
        // return question;
    }

    public Boolean delete(Question question) {

        this.questionRepository.delete(question);
        return true;
    }


    /* Views Counting */
    @Transactional
    public int updateView(Long id) {
        return questionRepository.updateView(id);
    }
}
