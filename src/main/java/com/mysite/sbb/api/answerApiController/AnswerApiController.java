package com.mysite.sbb.api.answerApiController;

import com.mysite.sbb.controller.answerController.AnswerForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.siteUser.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerApiController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public ResponseEntity<AnswerApiForm> createAnswer(Model model, @PathVariable("id") Long id,
                                       @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {

        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {

            throw new IllegalArgumentException("잘못된 입력 값");
        }
        Answer answer = this.answerService.create(question, answerForm.getContent(), siteUser);

        AnswerApiForm answerApiForm = new AnswerApiForm(answerForm.getContent());
        return ResponseEntity.ok(answerApiForm);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public ResponseEntity<AnswerForm> answerModify(AnswerForm answerForm, @PathVariable("id") Long id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return ResponseEntity.ok(answerForm);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/modify/{id}")
    public ResponseEntity<AnswerApiForm> answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());

        AnswerApiForm answerApiForm = new AnswerApiForm(answerForm.getContent());
        return ResponseEntity.ok(answerApiForm);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public Long answerDelete(Principal principal, @PathVariable("id")Long id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return answer.getQuestion().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public ResponseEntity<AnswerQuestionId> answerVote(Principal principal, @PathVariable("id") Long id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);

        AnswerQuestionId answerQuestionId = new AnswerQuestionId(answer.getQuestion().getId(), answer.getId());
        return ResponseEntity.ok(answerQuestionId);
    }

}
