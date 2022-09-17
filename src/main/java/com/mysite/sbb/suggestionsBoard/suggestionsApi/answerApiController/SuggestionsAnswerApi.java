package com.mysite.sbb.suggestionsBoard.suggestionsApi.answerApiController;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsDeleteInfoDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsModifyInfoDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsSuccessDto;
import com.mysite.sbb.suggestionsBoard.suggestionsController.answerController.SuggestionsAnswerForm;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/suggestions/answers")
@RequiredArgsConstructor
public class SuggestionsAnswerApi {

    private final SuggestionsQuestionService suggestionsQuestionService;
    private final SuggestionsAnswerService suggestionsAnswerService;

    @PostMapping("/{id}")
    public ResponseEntity<SuggestionsAnswerCreateForm> createAnswer(@PathVariable("id") Long id,
                                                                    @Valid @RequestBody SuggestionsAnswerForm suggestionsAnswerForm, BindingResult bindingResult) {

        Question question = suggestionsQuestionService.getQuestion(id);

        if (bindingResult.hasErrors()) {

            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        this.suggestionsAnswerService.create(question, suggestionsAnswerForm.getContent(), suggestionsAnswerForm.getUsername(), suggestionsAnswerForm.getPassword());

        SuggestionsAnswerCreateForm suggestionsAnswerCreateForm = new SuggestionsAnswerCreateForm(suggestionsAnswerForm.getContent(), suggestionsAnswerForm.getUsername());
        return ResponseEntity.ok(suggestionsAnswerCreateForm);
    }

//    @GetMapping("/modify/{id}")
//    public ResponseEntity<SuggestionsAnswerForm> answerModify(SuggestionsAnswerForm suggestionsAnswerForm, @PathVariable("id") Long id) {
//
//        Answer answer = this.suggestionsAnswerService.getAnswer(id);
//        suggestionsAnswerForm.setContent(answer.getContent());
//        return ResponseEntity.ok(suggestionsAnswerForm);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<SuggestionsAnswerModifyForm> answerModify(@Valid @RequestBody SuggestionsModifyInfoDto suggestionsModifyInfoDto, BindingResult bindingResult,
                                                                    @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Answer answer = this.suggestionsAnswerService.getAnswer(id);

        if (!answer.getPassword().equals(suggestionsModifyInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.suggestionsAnswerService.modify(answer, suggestionsModifyInfoDto.getContent());

        SuggestionsAnswerModifyForm suggestionsAnswerModifyForm = new SuggestionsAnswerModifyForm(suggestionsModifyInfoDto.getContent());
        return ResponseEntity.ok(suggestionsAnswerModifyForm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuggestionsSuccessDto> answerDelete(@Valid @RequestBody SuggestionsDeleteInfoDto suggestionsDeleteInfoDto, @PathVariable("id")Long id) {
        Answer answer = this.suggestionsAnswerService.getAnswer(id);

        if (!answer.getPassword().equals(suggestionsDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        SuggestionsSuccessDto suggestionsSuccessDto = new SuggestionsSuccessDto(this.suggestionsAnswerService.delete(answer));
        return ResponseEntity.ok(suggestionsSuccessDto);
    }

}
