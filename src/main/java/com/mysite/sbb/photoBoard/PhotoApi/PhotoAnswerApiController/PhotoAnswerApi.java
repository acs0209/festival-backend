package com.mysite.sbb.photoBoard.PhotoApi.PhotoAnswerApiController;

import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoDeleteInfoDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoModifyInfoDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoSuccessDto;
import com.mysite.sbb.photoBoard.PhotoController.answerController.PhotoAnswerForm;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/photo/answers")
@RequiredArgsConstructor
public class PhotoAnswerApi {

    private final PhotoQuestionService photoQuestionService;
    private final PhotoAnswerService photoAnswerService;

    // 댓글 작성 --> 게시글의 id 로 접근
    @PostMapping("/{id}")
    public ResponseEntity<PhotoCreateResponse> createAnswer(@PathVariable("id") Long id,
                                                              @Valid @RequestBody PhotoAnswerForm photoAnswerForm, BindingResult bindingResult) {

       PhotoQuestion photoQuestion = photoQuestionService.getQuestion(id);

        if (bindingResult.hasErrors()) {

            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        PhotoAnswer photoAnswer = this.photoAnswerService.create(photoQuestion, photoAnswerForm.getContent(), photoAnswerForm.getUsername(), photoAnswerForm.getPassword());

        PhotoCreateResponse photoCreateResponse = new PhotoCreateResponse(photoAnswerForm.getContent(), photoAnswerForm.getUsername(), photoAnswer.getDate());
        return ResponseEntity.ok(photoCreateResponse);
    }

    // 댓글 보기
//    @GetMapping("/modify/{id}")
//    public ResponseEntity<AnswerForm> answerModify(AnswerForm answerForm, @PathVariable("id") Long id) {
//
//        Answer answer = this.answerService.getAnswer(id);
//        answerForm.setContent(answer.getContent());
//        return ResponseEntity.ok(answerForm);
//    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PhotoAnswerModifyForm> answerModify(@Valid @RequestBody PhotoModifyInfoDto photoModifyInfoDto, BindingResult bindingResult,
                                                              @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
       PhotoAnswer photoAnswer = this.photoAnswerService.getPhotoAnswer(id);

        if (!photoAnswer.getPassword().equals(photoModifyInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.photoAnswerService.modify(photoAnswer, photoModifyInfoDto.getContent());

        PhotoAnswerModifyForm photoAnswerModifyForm = new PhotoAnswerModifyForm(photoModifyInfoDto.getContent());
        return ResponseEntity.ok(photoAnswerModifyForm);
    }

    // 댓글 삭제 --> form-data 로 보내야 함
    @DeleteMapping("/{id}")
    public ResponseEntity<PhotoSuccessDto> answerDelete(@Valid PhotoDeleteInfoDto photoDeleteInfoDto, @PathVariable("id") Long id) {
      PhotoAnswer photoAnswer = this.photoAnswerService.getPhotoAnswer(id);

        if (!photoAnswer.getPassword().equals(photoDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        PhotoSuccessDto photoSuccessDto = new PhotoSuccessDto(this.photoAnswerService.delete(photoAnswer));
        return ResponseEntity.ok(photoSuccessDto);
    }

}
