package com.mysite.sbb.photoBoard.PhotoApi.PhotoCommentApiController;

import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoDeleteInfoDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoModifyInfoDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoSuccessDto;
import com.mysite.sbb.photoBoard.PhotoController.commentController.PhotoCommentForm;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoComment;
import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo/comments")
public class PhotoCommentApi {

    private final PhotoAnswerService photoAnswerService;
    private final PhotoCommentService photoCommentService;


    // 답변 댓글 생성
    @PostMapping(value = "/answers/{id}")
    public ResponseEntity<PhotoCommentCreateForm> createAnswerComment(@PathVariable("id") Long id, @Valid @RequestBody PhotoCommentForm photoCommentForm,
                                                                      BindingResult bindingResult) {

        Optional<PhotoAnswer> answer = Optional.ofNullable(this.photoAnswerService.getPhotoAnswer(id));

        if (answer.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값입니다.");
            }
           PhotoComment c = this.photoCommentService.create(answer.get(), photoCommentForm.getContent(), photoCommentForm.getUsername(), photoCommentForm.getPassword());
            // 이름이랑 제목만 전달
            PhotoCommentCreateForm photoCommentCreateForm = new PhotoCommentCreateForm(photoCommentForm.getContent(), photoCommentForm.getUsername(), c.getDate());
            return ResponseEntity.ok(photoCommentCreateForm);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }


//    @GetMapping("/modify/{id}")
//    public ResponseEntity<CommentForm> modifyComment(CommentForm commentForm, @PathVariable("id") Long id) {
//        Optional<Comment> comment = this.commentService.getComment(id);
//        if (comment.isPresent()) {
//            Comment c = comment.get();
//
//            commentForm.setContent(c.getContent());
//        }
//
//        return ResponseEntity.ok(commentForm);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoCommentModifyForm> modifyComment(@Valid @RequestBody PhotoModifyInfoDto photoModifyInfoDto, BindingResult bindingResult,
                                                                @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Optional<PhotoComment> comment = this.photoCommentService.getComment(id);
        if (comment.isPresent()) {
            PhotoComment c = comment.get();

            if (!c.getPassword().equals(photoModifyInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }

            c = this.photoCommentService.modify(c, photoModifyInfoDto.getContent());

            PhotoCommentModifyForm photoCommentModifyForm = new PhotoCommentModifyForm(photoModifyInfoDto.getContent());
            return ResponseEntity.ok(photoCommentModifyForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

    // 대댓글 삭제 --> form-data로 받아야 한다.
    @DeleteMapping("/{id}")
    public ResponseEntity<PhotoSuccessDto> deleteComment(@PathVariable("id") Long id, @Valid PhotoDeleteInfoDto deleteInfoDto) {
        Optional<PhotoComment> comment = this.photoCommentService.getComment(id);
        if (comment.isPresent()) {
            PhotoComment c = comment.get();

            if (!c.getPassword().equals(deleteInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            }

            PhotoSuccessDto photoSuccessDto = new PhotoSuccessDto(this.photoCommentService.delete(c));
            return ResponseEntity.ok(photoSuccessDto);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

}
