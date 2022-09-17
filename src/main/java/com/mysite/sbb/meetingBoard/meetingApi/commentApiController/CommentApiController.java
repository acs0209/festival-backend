package com.mysite.sbb.meetingBoard.meetingApi.commentApiController;

import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingDeleteInfoDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingModifyInfoDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingSuccessDto;
import com.mysite.sbb.meetingBoard.meetingController.commentController.CommentForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.meetingBoard.meetingService.AnswerService;
import com.mysite.sbb.meetingBoard.meetingService.CommentService;
import com.mysite.sbb.meetingBoard.meetingService.QuestionService;
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
@RequestMapping("/meeting/comments")
public class CommentApiController {

    private final AnswerService answerService;
    private final CommentService commentService;

    // 답변 댓글 생성
    @PostMapping(value = "/answers/{id}")
    public ResponseEntity<CommentCreateForm> createAnswerComment(@PathVariable("id") Long id, @Valid @RequestBody CommentForm commentForm,
                                              BindingResult bindingResult) {

        Optional<Answer> answer = Optional.ofNullable(this.answerService.getAnswer(id));

        if (answer.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값입니다.");
            }
            Comment c = this.commentService.create(answer.get(), commentForm.getContent(), commentForm.getUsername(), commentForm.getPassword());

            CommentCreateForm commentCreateForm = new CommentCreateForm(commentForm.getContent(), commentForm.getUsername(), c.getCreateDate());
            return ResponseEntity.ok(commentCreateForm);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

//    // 질문 댓글 생성
//    @PostMapping(value = "/post/{id}")
//    public ResponseEntity<CommentCreateForm> createQuestionComment(@PathVariable("id") Long id, @Valid @RequestBody CommentForm commentForm,
//                                        BindingResult bindingResult) {
//
//        Optional<Question> question = Optional.ofNullable(this.questionService.getQuestion(id));
//
//        if (question.isPresent()) {
//            if (bindingResult.hasErrors()) {
//                throw new IllegalArgumentException("잘못된 입력 값입니다.");
//            }
//            Comment c = this.commentService.create(question.get(), commentForm.getContent(), commentForm.getUsername(), commentForm.getPassword());
//
//            CommentCreateForm commentCreateForm = new CommentCreateForm(commentForm.getContent(), commentForm.getUsername());
//            return ResponseEntity.ok(commentCreateForm);
//
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
//        }
//
//    }

//    @GetMapping("/{id}")
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
    public ResponseEntity<CommentModifyForm> modifyComment(@Valid @RequestBody MeetingModifyInfoDto meetingModifyInfoDto, BindingResult bindingResult,
                                                           @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();

            if (!c.getPassword().equals(meetingModifyInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }

            c = this.commentService.modify(c, meetingModifyInfoDto.getContent());

            CommentModifyForm commentModifyForm = new CommentModifyForm(meetingModifyInfoDto.getContent());
            return ResponseEntity.ok(commentModifyForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MeetingSuccessDto> deleteComment(@PathVariable("id") Long id, @Valid @RequestBody MeetingDeleteInfoDto meetingDeleteInfoDto) {
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();

            if (!c.getPassword().equals(meetingDeleteInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            }

            MeetingSuccessDto meetingSuccessDto = new MeetingSuccessDto(this.commentService.delete(c));
            return ResponseEntity.ok(meetingSuccessDto);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"요청하신 데이터를 찾을 수 없습니다.");
        }


    }
}
