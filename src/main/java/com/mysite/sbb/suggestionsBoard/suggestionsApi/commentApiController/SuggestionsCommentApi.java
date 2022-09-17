package com.mysite.sbb.suggestionsBoard.suggestionsApi.commentApiController;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsDeleteInfoDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsModifyInfoDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsSuccessDto;
import com.mysite.sbb.suggestionsBoard.suggestionsController.commentController.SuggestionsCommentForm;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsCommentService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsQuestionService;
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
@RequestMapping("/suggestions/comments")
public class SuggestionsCommentApi {

    private final SuggestionsAnswerService suggestionsAnswerService;
    private final SuggestionsCommentService suggestionsCommentService;
    private final SuggestionsQuestionService suggestionsQuestionService;

    // 답변 댓글 가져오기
//    @GetMapping("/answer/list/{answerId}")
//    public ResponseEntity<Map<String, Object>> answerCommentList(@PathVariable("answerId") Long id,
//                                  @RequestParam(value = "page", defaultValue = "0") int page) {
//
//        Answer answer = answerService.getAnswer(id);
//        AnswerDto answerDto = new AnswerDto(answer.getId(), answer.getContent(), answer.getCreateDate(), answer.getUsername(), answer.getVoter().size(), answer.getCommentList());
//        Page<Comment> commentPage = commentService.getAnswerCommentList(page, id);
//
//        if (commentPage.getNumberOfElements() == 0 && page != 0) {
//            throw new IllegalArgumentException("잘못된 입력 값입니다");
//        }
//
//        Page<CommentDto> commentDtoPage = commentPage.map(
//                post -> new CommentDto (
//                        post.getId(),post.getContent(),post.getCreateDate(),
//                        post.getModifyDate(),post.getUsername()
//                ));
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("answer", answerDto);
//        result.put("comments", commentDtoPage);
//
//        return ResponseEntity.ok(result);
//    }
//    // 질문 댓글 가져오기
//    @GetMapping("/question/list/{questionId}")
//    public ResponseEntity<Map<String, Object>> questionCommentList(@PathVariable("questionId") Long id,
//                                    @RequestParam(value = "page", defaultValue = "0") int page) {
//
//        Question question = questionService.getQuestion(id);
//        QuestionDto questionDto = new QuestionDto(question.getId(), question.getSubject(), question.getContent(), question.getCreateDate(), question.getModifyDate(),question.getUsername() ,question.getView(), question.getVoter().size());
//        Page<Comment> commentPage = commentService.getQuestionCommentList(page, id);
//
//        if (commentPage.getNumberOfElements() == 0 && page != 0) {
//            throw new IllegalArgumentException("잘못된 입력 값입니다.");
//        }
//
//        Page<CommentDto> commentDtoPage = commentPage.map(
//                post -> new CommentDto (
//                        post.getId(),post.getContent(),post.getCreateDate(),
//                        post.getModifyDate(),post.getUsername()
//                ));
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("question", questionDto);
//        result.put("comments", commentDtoPage);
//
//        return ResponseEntity.ok(result);
//    }

    // 답변 댓글 생성
    @PostMapping(value = "/answers/{id}")
    public ResponseEntity<SuggestionsCommentCreateForm> createAnswerComment(@PathVariable("id") Long id, @Valid @RequestBody SuggestionsCommentForm suggestionsCommentForm,
                                                                            BindingResult bindingResult) {

        Optional<Answer> answer = Optional.ofNullable(this.suggestionsAnswerService.getAnswer(id));

        if (answer.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값입니다.");
            }
            Comment c = this.suggestionsCommentService.create(answer.get(), suggestionsCommentForm.getContent(), suggestionsCommentForm.getUsername(), suggestionsCommentForm.getPassword());

            SuggestionsCommentCreateForm suggestionsCommentCreateForm = new SuggestionsCommentCreateForm(suggestionsCommentForm.getContent(), suggestionsCommentForm.getUsername());
            return ResponseEntity.ok(suggestionsCommentCreateForm);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

    // 질문 댓글 생성
    @PostMapping(value = "/post/{id}")
    public ResponseEntity<SuggestionsCommentCreateForm> createQuestionComment(@PathVariable("id") Long id, @Valid @RequestBody SuggestionsCommentForm suggestionsCommentForm,
                                                                              BindingResult bindingResult) {

        Optional<Question> question = Optional.ofNullable(this.suggestionsQuestionService.getQuestion(id));

        if (question.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값입니다.");
            }
            Comment c = this.suggestionsCommentService.create(question.get(), suggestionsCommentForm.getContent(), suggestionsCommentForm.getUsername(), suggestionsCommentForm.getPassword());

            SuggestionsCommentCreateForm suggestionsCommentCreateForm = new SuggestionsCommentCreateForm(suggestionsCommentForm.getContent(), suggestionsCommentForm.getUsername());
            return ResponseEntity.ok(suggestionsCommentCreateForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }

    }

//    @GetMapping("/{id}")
//    public ResponseEntity<SuggestionsCommentForm> modifyComment(SuggestionsCommentForm suggestionsCommentForm, @PathVariable("id") Long id) {
//        Optional<Comment> comment = this.suggestionsCommentService.getComment(id);
//        if (comment.isPresent()) {
//            Comment c = comment.get();
//
//            suggestionsCommentForm.setContent(c.getContent());
//        }
//
//        return ResponseEntity.ok(suggestionsCommentForm);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<SuggestionsCommentModifyForm> modifyComment(@Valid @RequestBody SuggestionsModifyInfoDto suggestionsModifyInfoDto, BindingResult bindingResult,
                                                                      @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }
        Optional<Comment> comment = this.suggestionsCommentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();

            if (!c.getPassword().equals(suggestionsModifyInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }

            c = this.suggestionsCommentService.modify(c, suggestionsModifyInfoDto.getContent());

            SuggestionsCommentModifyForm suggestionsCommentModifyForm = new SuggestionsCommentModifyForm(suggestionsModifyInfoDto.getContent());
            return ResponseEntity.ok(suggestionsCommentModifyForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuggestionsSuccessDto> deleteComment(@PathVariable("id") Long id, @Valid @RequestBody SuggestionsDeleteInfoDto suggestionsDeleteInfoDto) {
        Optional<Comment> comment = this.suggestionsCommentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();

            if (!c.getPassword().equals(suggestionsDeleteInfoDto.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            }

            SuggestionsSuccessDto suggestionsSuccessDto = new SuggestionsSuccessDto(this.suggestionsCommentService.delete(c));
            return ResponseEntity.ok(suggestionsSuccessDto);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"요청하신 데이터를 찾을 수 없습니다.");
        }


    }

}
