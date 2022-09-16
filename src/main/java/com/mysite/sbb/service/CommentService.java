package com.mysite.sbb.service;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.comment.CommentRepository;
import com.mysite.sbb.entity.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentRepository commentRepository;

    // 답변 댓글
    public Page<Comment> getAnswerCommentList(int page, Long id) {
        Answer answer = answerService.getAnswer(id);
        Pageable pageable = PageRequest.of(page, 10);
        return this.commentRepository.findAllByAnswer(answer, pageable);
    }

    // 질문 댓글
    public Page<Comment> getQuestionCommentList(int page, Long id) {
        Question question = questionService.getQuestion(id);
        Pageable pageable = PageRequest.of(page, 10);
        return this.commentRepository.findAllByQuestion(question, pageable);
    }


    // 답변 댓글
    public Comment create(Answer answer, String content, String username, String password) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setAnswer(answer);
        c.setUsername(username);
        c.setPassword(password);
        c = this.commentRepository.save(c);
        return c;
    }

    // 질문 댓글
    public Comment create(Question question, String content, String username, String password) {
        Comment c = new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setQuestion(question);
        c.setUsername(username);
        c.setPassword(password);
        c = this.commentRepository.save(c);
        return c;
    }

    public Optional<Comment> getComment(Long id) {

        return commentRepository.findById(id);
    }

    public Comment modify(Comment c, String content) {
        c.setContent(content);
        c = this.commentRepository.save(c);
        return c;
    }

    public Boolean delete(Comment c) {

        this.commentRepository.delete(c);
        return true;
    }

}
