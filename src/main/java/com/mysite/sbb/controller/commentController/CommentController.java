package com.mysite.sbb.controller.commentController;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.siteUser.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.CommentService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    // 답변
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/answer/{id}")
    public String createAnswerComment(CommentForm commentForm) {

        return "comment_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/answer/{id}")
    public String createAnswerComment(@PathVariable("id") Long id, @Valid CommentForm commentForm,
                                      BindingResult bindingResult, Principal principal) {

//        Optional<Question> question = Optional.ofNullable(this.questionService.getQuestion(id));
        Optional<Answer> answer = Optional.ofNullable(this.answerService.getAnswer(id));
        Optional<SiteUser> user = Optional.ofNullable(this.userService.getUser(principal.getName()));

        if ( user.isPresent() && answer.isPresent()) {
            if (bindingResult.hasErrors()) {
                return "comment_form";
            }
            Comment c = this.commentService.create(answer.get(), user.get(), commentForm.getContent());
            return String.format("redirect:/question/detail/%s", c.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
    }

    // 질문
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/question/{id}")
    public String createQuestionComment(CommentForm commentForm) {

        return "comment_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/question/{id}")
    public String createQuestionComment(@PathVariable("id") Long id, @Valid CommentForm commentForm,
                                        BindingResult bindingResult, Principal principal) {

        Optional<Question> question = Optional.ofNullable(this.questionService.getQuestion(id));
        Optional<SiteUser> user = Optional.ofNullable(this.userService.getUser(principal.getName()));

        if (question.isPresent() && user.isPresent()) {
            if (bindingResult.hasErrors()) {
                return "comment_form";
            }
            Comment c = this.commentService.create(question.get(), user.get(), commentForm.getContent());
            return String.format("redirect:/question/detail/%s", c.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            commentForm.setContent(c.getContent());
        }
        return "comment_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
                                @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "comment_form";
        }
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            c = this.commentService.modify(c, commentForm.getContent());
            return String.format("redirect:/question/detail/%s", c.getQuestionId());

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Long id) {
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            }
            this.commentService.delete(c);
            return String.format("redirect:/question/detail/%s", c.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        }
    }

}
