package com.mysite.sbb.api.answerApiController;

import com.mysite.sbb.configDto.DeleteInfoDto;
import com.mysite.sbb.configDto.ModifyInfoDto;
import com.mysite.sbb.configDto.SuccessDto;
import com.mysite.sbb.controller.answerController.AnswerForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerApiController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public ResponseEntity<AnswerApiForm> createAnswer(@PathVariable("id") Long id,
                                       @Valid AnswerForm answerForm, BindingResult bindingResult) {

        Question question = questionService.getQuestion(id);

        if (bindingResult.hasErrors()) {

            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        this.answerService.create(question, answerForm.getContent(), answerForm.getUsername(), answerForm.getPassword());

        AnswerApiForm answerApiForm = new AnswerApiForm(answerForm.getContent());
        return ResponseEntity.ok(answerApiForm);
    }

    @GetMapping("/modify/{id}")
    public ResponseEntity<AnswerForm> answerModify(AnswerForm answerForm, @PathVariable("id") Long id) {

        Answer answer = this.answerService.getAnswer(id);
        answerForm.setContent(answer.getContent());
        return ResponseEntity.ok(answerForm);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<AnswerApiForm> answerModify(@Valid ModifyInfoDto modifyInfoDto, BindingResult bindingResult,
                                                      @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getPassword().equals(modifyInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, modifyInfoDto.getContent());

        AnswerApiForm answerApiForm = new AnswerApiForm(modifyInfoDto.getContent());
        return ResponseEntity.ok(answerApiForm);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessDto> answerDelete(@Valid DeleteInfoDto deleteInfoDto, @PathVariable("id")Long id) {
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getPassword().equals(deleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        SuccessDto successDto = new SuccessDto(this.answerService.delete(answer));
        return ResponseEntity.ok(successDto);
    }

}
