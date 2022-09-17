package com.mysite.sbb.suggestionsBoard.suggestionsApi.questionApiController;

import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.suggestionsBoard.suggestionsApi.answerApiController.SuggestionsAnswerDto;
import com.mysite.sbb.suggestionsBoard.suggestionsApi.commentApiController.SuggestionsCommentDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsDeleteInfoDto;
import com.mysite.sbb.suggestionsBoard.suggestionsConfigDto.SuggestionsSuccessDto;
import com.mysite.sbb.suggestionsBoard.suggestionsController.questionController.SuggestionsQuestionForm;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsCommentService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/suggestions/post")
@RequiredArgsConstructor
public class SuggestionsQuestionApi {

    private final SuggestionsQuestionService suggestionsQuestionService;
    private final SuggestionsAnswerService suggestionsAnswerService;
    private final SuggestionsCommentService suggestionsCommentService;

//    @GetMapping("/{id}")
//    public ResponseEntity<QuestionApiForm> question(@PathVariable("id") Long id) {
//        Question question = questionService.getQuestion(id);
//        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent(), question.getUsername());
//        return ResponseEntity.ok(questionApiForm);
//    }

    @GetMapping("/list")
    public ResponseEntity<Page<SuggestionsQuestionDto>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<Question> paging = suggestionsQuestionService.getList(page, kw);


        if (paging.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Page<SuggestionsQuestionDto> questionDtoPage = paging.map(
                post -> new SuggestionsQuestionDto(
                      post.getId(),post.getSubject(),post.getContent(),post.getCreateDate(),
                      post.getUsername(),post.getView()
                ));

        return ResponseEntity.ok(questionDtoPage);
    }

    /*
     * 상세 질문을 보기 위해 데이터를 가공하는 함수
       답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
     * */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable("id") Long id,
                         @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<Answer> pagingAnswer = suggestionsAnswerService.getList(page, id);
        Question question = this.suggestionsQuestionService.getQuestion(id);
        Page<Comment> commentPage = suggestionsCommentService.getQuestionCommentList(page, id);

        if (pagingAnswer.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        suggestionsQuestionService.updateView(id); // views ++ 조회수 처리
        SuggestionsQuestionDto suggestionsQuestionDto = new SuggestionsQuestionDto(question.getId(), question.getSubject(), question.getContent(),
                question.getCreateDate(),question.getUsername() ,question.getView());

        Page<SuggestionsAnswerDto> answerPagingDto = pagingAnswer.map(
                post -> new SuggestionsAnswerDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername(),
                        post.getCommentList()
                ));
        Page<SuggestionsCommentDto> commentDtoPage = commentPage.map(
                post -> new SuggestionsCommentDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("question", suggestionsQuestionDto);
        result.put("answers", answerPagingDto);
        result.put("questionComments", commentDtoPage);

        return ResponseEntity.ok(result);
    }

    // 글 생성 api
    @PostMapping("/")
    public ResponseEntity<SuggestionsQuestionApiForm> questionCreate(@Valid @RequestBody SuggestionsQuestionForm suggestionsQuestionForm,
                                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        suggestionsQuestionService.create(suggestionsQuestionForm.getSubject(), suggestionsQuestionForm.getContent(), suggestionsQuestionForm.getUsername(), suggestionsQuestionForm.getPassword());

        SuggestionsQuestionApiForm suggestionsQuestionApiForm = new SuggestionsQuestionApiForm(suggestionsQuestionForm.getSubject(), suggestionsQuestionForm.getContent(), suggestionsQuestionForm.getUsername());
        return ResponseEntity.ok(suggestionsQuestionApiForm);
    }

    // 수정 내용 조회 api
//    @GetMapping("/modify/{id}")
//    public ResponseEntity<QuestionApiForm> questionModify(QuestionForm questionForm, @PathVariable("id") Long id) {
//        Question question = this.questionService.getQuestion(id);
//        questionForm.setSubject(question.getSubject());
//        questionForm.setContent(question.getContent());
//
//        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent(), question.getUsername());
//        return ResponseEntity.ok(questionApiForm);
//    }

    // 수정 api
    @PutMapping("/{id}")
    public ResponseEntity<SuggestionsQuestionModifyForm> questionModify(@Valid @RequestBody SuggestionsQuestionForm suggestionsQuestionForm, BindingResult bindingResult,
                                                                        @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Question question = this.suggestionsQuestionService.getQuestion(id);

        if (!question.getPassword().equals(suggestionsQuestionForm.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
        }

        this.suggestionsQuestionService.modify(question, suggestionsQuestionForm.getSubject(), suggestionsQuestionForm.getContent());
        SuggestionsQuestionModifyForm suggestionsQuestionModifyForm = new SuggestionsQuestionModifyForm(question.getSubject(), question.getContent());
        return ResponseEntity.ok(suggestionsQuestionModifyForm);
    }

    // 삭제 api
    @DeleteMapping("/{id}")
    public ResponseEntity<SuggestionsSuccessDto> questionDelete(@Valid @RequestBody SuggestionsDeleteInfoDto suggestionsDeleteInfoDto, @PathVariable("id") Long id) {
        Question question = this.suggestionsQuestionService.getQuestion(id);

        if (!question.getPassword().equals(suggestionsDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        SuggestionsSuccessDto suggestionsSuccessDto = new SuggestionsSuccessDto(this.suggestionsQuestionService.delete(question));
        return ResponseEntity.ok(suggestionsSuccessDto);
    }
}
