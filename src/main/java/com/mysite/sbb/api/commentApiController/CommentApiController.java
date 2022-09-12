package com.mysite.sbb.api.commentApiController;

import com.mysite.sbb.api.answerApiController.AnswerDto;
import com.mysite.sbb.api.questionApiController.QuestionDto;
import com.mysite.sbb.controller.commentController.CommentForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.siteUser.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.CommentService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentApiController {

    private final AnswerService answerService;
    private final CommentService commentService;
    private final QuestionService questionService;
    private final UserService userService;

    // 답변 댓글 가져오기
    @GetMapping("/answer/list/{answerId}")
    public ResponseEntity<Map<String, Object>> answerCommentList(@PathVariable("answerId") Long id,
                                  @RequestParam(value = "page", defaultValue = "0") int page) {

        Answer answer = answerService.getAnswer(id);
        AnswerDto answerDto = new AnswerDto(answer.getId(), answer.getContent(), answer.getCreateDate(), answer.getModifyDate(), answer.getAuthor().getUsername(), answer.getVoter().size());
        Page<Comment> commentPage = commentService.getAnswerCommentList(page, id);

        if (commentPage.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        Page<CommentDto> commentDtoPage = commentPage.map(
                post -> new CommentDto (
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getModifyDate(),post.getAuthor().getUsername()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("answer", answerDto);
        result.put("comments", commentDtoPage);

        return ResponseEntity.ok(result);
    }
    // 질문 댓글 가져오기
    @GetMapping("/question/list/{questionId}")
    public ResponseEntity<Map<String, Object>> questionCommentList(@PathVariable("questionId") Long id,
                                    @RequestParam(value = "page", defaultValue = "0") int page) {

        Question question = questionService.getQuestion(id);
        QuestionDto questionDto = new QuestionDto(question.getId(), question.getSubject(), question.getContent(), question.getCreateDate(), question.getModifyDate(),question.getAuthor().getUsername() ,question.getView(), question.getVoter().size());
        Page<Comment> commentPage = commentService.getQuestionCommentList(page, id);

        if (commentPage.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        Page<CommentDto> commentDtoPage = commentPage.map(
                post -> new CommentDto (
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getModifyDate(),post.getAuthor().getUsername()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("question", questionDto);
        result.put("comments", commentDtoPage);

        return ResponseEntity.ok(result);
    }


    // 답변 댓글
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/answer/{id}")
    public ResponseEntity<CommentForm> createAnswerComment(CommentForm commentForm) {

        return ResponseEntity.ok(commentForm);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/answer/{id}")
    public ResponseEntity createAnswerComment(@PathVariable("id") Long id, @Valid CommentForm commentForm,
                                              BindingResult bindingResult, Principal principal) {

        Optional<Answer> answer = Optional.ofNullable(this.answerService.getAnswer(id));
        Optional<SiteUser> user = Optional.ofNullable(this.userService.getUser(principal.getName()));

        if ( user.isPresent() && answer.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값");
            }
            Comment c = this.commentService.create(answer.get(), user.get(), commentForm.getContent());

            CommentApiForm commentApiForm = new CommentApiForm(commentForm.getContent(), c.getQuestionId());
            return ResponseEntity.ok(commentApiForm);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity.not.found");
        }
    }

    // 질문 댓글
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/question/{id}")
    public ResponseEntity<CommentForm> createQuestionComment(CommentForm commentForm) {

        return ResponseEntity.ok(commentForm);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/question/{id}")
    public ResponseEntity createQuestionComment(@PathVariable("id") Long id, @Valid CommentForm commentForm,
                                        BindingResult bindingResult, Principal principal) {

        Optional<Question> question = Optional.ofNullable(this.questionService.getQuestion(id));
        Optional<SiteUser> user = Optional.ofNullable(this.userService.getUser(principal.getName()));

        if (question.isPresent() && user.isPresent()) {
            if (bindingResult.hasErrors()) {
                throw new IllegalArgumentException("잘못된 입력 값");
            }
            Comment c = this.commentService.create(question.get(), user.get(), commentForm.getContent());

            CommentApiForm commentApiForm = new CommentApiForm(commentForm.getContent(), c.getQuestionId());
            return ResponseEntity.ok(commentApiForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity.not.found");
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public ResponseEntity<CommentForm> modifyComment(CommentForm commentForm, @PathVariable("id") Long id, Principal principal) {
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            commentForm.setContent(c.getContent());
        }

        return ResponseEntity.ok(commentForm);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/modify/{id}")
    public ResponseEntity<CommentApiForm> modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
                                @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
            c = this.commentService.modify(c, commentForm.getContent());

            CommentApiForm commentApiForm = new CommentApiForm(commentForm.getContent(), c.getQuestionId());
            return ResponseEntity.ok(commentApiForm);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity.not.found");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteComment(Principal principal, @PathVariable("id") Long id) {
        Optional<Comment> comment = this.commentService.getComment(id);
        if (comment.isPresent()) {
            Comment c = comment.get();
            if (!c.getAuthor().getUsername().equals(principal.getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
            }
            this.commentService.delete(c);
            return ResponseEntity.ok(c.getQuestionId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity.not.found");
        }
    }

}
