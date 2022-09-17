package com.mysite.sbb.meetingBoard.meetingApi.questionApiController;

import com.mysite.sbb.meetingBoard.meetingApi.answerApiController.AnswerDto;
import com.mysite.sbb.meetingBoard.meetingApi.commentApiController.CommentDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingDeleteInfoDto;
import com.mysite.sbb.meetingBoard.meetingConfigDto.MeetingSuccessDto;
import com.mysite.sbb.meetingBoard.meetingController.questionController.QuestionForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.comment.Comment;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.meetingBoard.meetingService.AnswerService;
import com.mysite.sbb.meetingBoard.meetingService.CommentService;
import com.mysite.sbb.meetingBoard.meetingService.QuestionService;
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
@RequestMapping("/meeting/post")
@RequiredArgsConstructor
public class QuestionApiController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final CommentService commentService;

//    @GetMapping("/{id}")
//    public ResponseEntity<QuestionApiForm> question(@PathVariable("id") Long id) {
//        Question question = questionService.getQuestion(id);
//        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent(), question.getUsername());
//        return ResponseEntity.ok(questionApiForm);
//    }

    @GetMapping("/list")
    public ResponseEntity<Page<QuestionDto>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<Question> paging = questionService.getList(page, kw);


        if (paging.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Page<QuestionDto> questionDtoPage = paging.map(
                post -> new QuestionDto(
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
        Page<Answer> pagingAnswer = answerService.getList(page, id);
        Question question = this.questionService.getQuestion(id);
        Page<Comment> commentPage = commentService.getQuestionCommentList(page, id);

        if (pagingAnswer.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        questionService.updateView(id); // views ++ 조회수 처리
        QuestionDto questionDto = new QuestionDto(question.getId(), question.getSubject(), question.getContent(),
                question.getCreateDate(),question.getUsername() ,question.getView());

        Page<AnswerDto> answerPagingDto = pagingAnswer.map(
                post -> new AnswerDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername(),
                        post.getCommentList()
                ));
        Page<CommentDto> commentDtoPage = commentPage.map(
                post -> new CommentDto (
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("question", questionDto);
        result.put("answers", answerPagingDto);
        result.put("questionComments", commentDtoPage);

        return ResponseEntity.ok(result);
    }

    // 글 생성 api
    @PostMapping("/")
    public ResponseEntity<QuestionApiForm> questionCreate(@Valid @RequestBody QuestionForm questionForm,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Question question = questionService.create(questionForm.getSubject(), questionForm.getContent(), questionForm.getUsername(), questionForm.getPassword());

        QuestionApiForm questionApiForm = new QuestionApiForm(questionForm.getSubject(), questionForm.getContent(), questionForm.getUsername(), question.getCreateDate());
        return ResponseEntity.ok(questionApiForm);
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
    public ResponseEntity<QuestionModifyForm> questionModify(@Valid @RequestBody QuestionRequestDto questionRequestDto, BindingResult bindingResult,
                                            @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Question question = this.questionService.getQuestion(id);

        if (!question.getPassword().equals(questionRequestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
        }

        this.questionService.modify(question, questionRequestDto.getSubject(), questionRequestDto.getContent());
        QuestionModifyForm questionModifyForm = new QuestionModifyForm(question.getSubject(), question.getContent());
        return ResponseEntity.ok(questionModifyForm);
    }

    // 삭제 api
    @DeleteMapping("/{id}")
    public ResponseEntity<MeetingSuccessDto> questionDelete(@Valid @RequestBody MeetingDeleteInfoDto meetingDeleteInfoDto, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getPassword().equals(meetingDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        MeetingSuccessDto meetingSuccessDto = new MeetingSuccessDto(this.questionService.delete(question));
        return ResponseEntity.ok(meetingSuccessDto);
    }
}
