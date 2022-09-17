package com.mysite.sbb.meetingBoard.meetingApi.answerApiController;

import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingDeleteInfoDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingModifyInfoDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingSuccessDto;
import com.mysite.sbb.meetingBoard.meetingController.answerController.AnswerForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.meetingBoard.meetingService.AnswerService;
import com.mysite.sbb.meetingBoard.meetingService.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/meeting/answers")
@RequiredArgsConstructor
public class AnswerApiController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/{id}")
    public ResponseEntity<AnswerCreateForm> createAnswer(@PathVariable("id") Long id,
                                                         @Valid @RequestBody AnswerForm answerForm, BindingResult bindingResult) {

        Question question = questionService.getQuestion(id);

        if (bindingResult.hasErrors()) {

            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Answer answer = this.answerService.create(question, answerForm.getContent(), answerForm.getUsername(), answerForm.getPassword());

        AnswerCreateForm answerCreateForm = new AnswerCreateForm(answerForm.getContent(), answerForm.getUsername(), answer.getCreateDate());
        return ResponseEntity.ok(answerCreateForm);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<AnswerForm> answerModify(AnswerForm answerForm, @PathVariable("id") Long id) {
//
//        Answer answer = this.answerService.getAnswer(id);
//        answerForm.setContent(answer.getContent());
//        return ResponseEntity.ok(answerForm);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<AnswerModifyForm> answerModify(@Valid @RequestBody MeetingModifyInfoDto meetingModifyInfoDto, BindingResult bindingResult,
                                                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getPassword().equals(meetingModifyInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, meetingModifyInfoDto.getContent());

        AnswerModifyForm answerModifyForm = new AnswerModifyForm(meetingModifyInfoDto.getContent());
        return ResponseEntity.ok(answerModifyForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MeetingSuccessDto> answerDelete(@Valid @RequestBody MeetingDeleteInfoDto meetingDeleteInfoDto, @PathVariable("id")Long id) {
        Answer answer = this.answerService.getAnswer(id);

        if (!answer.getPassword().equals(meetingDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        MeetingSuccessDto meetingSuccessDto = new MeetingSuccessDto(this.answerService.delete(answer));
        return ResponseEntity.ok(meetingSuccessDto);
    }

}
