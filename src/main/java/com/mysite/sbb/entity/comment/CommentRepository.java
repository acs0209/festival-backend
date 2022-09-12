package com.mysite.sbb.entity.comment;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByQuestion(Question question, Pageable pageable);

    Page<Comment> findAllByAnswer(Answer question, Pageable pageable);

}
