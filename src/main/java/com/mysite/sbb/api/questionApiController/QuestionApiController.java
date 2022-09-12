package com.mysite.sbb.api.questionApiController;

import com.mysite.sbb.api.answerApiController.AnswerDto;
import com.mysite.sbb.controller.answerController.AnswerForm;
import com.mysite.sbb.controller.questionController.QuestionForm;
import com.mysite.sbb.entity.answer.Answer;
import com.mysite.sbb.entity.question.Question;
import com.mysite.sbb.entity.siteUser.SiteUser;
import com.mysite.sbb.service.AnswerService;
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

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionApiController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;


    @GetMapping("/{id}")
    public ResponseEntity<QuestionApiForm> question(@PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent());
        return ResponseEntity.ok(questionApiForm);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<QuestionDto>> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<Question> paging = questionService.getList(page, kw);

        if (paging.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        Page<QuestionDto> questionDtoPage = paging.map(
                post -> new QuestionDto(
                      post.getId(),post.getSubject(),post.getContent(),post.getCreateDate(),
                      post.getModifyDate(),post.getAuthor().getUsername(),post.getView(),post.getVoter().size()
                ));

        return ResponseEntity.ok(questionDtoPage);
    }

    /*
     * 상세 질문을 보기 위해 데이터를 가공하는 함수
       답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
     * */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable("id") Long id, AnswerForm answerForm,
                         @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<Answer> pagingAnswer = answerService.getList(page, id);
        Question question = this.questionService.getQuestion(id);

        if (pagingAnswer.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        questionService.updateView(id); // views ++ 조회수 처리
        QuestionDto questionDto = new QuestionDto(question.getId(), question.getSubject(), question.getContent(), question.getCreateDate(), question.getModifyDate(),question.getAuthor().getUsername() ,question.getView(), question.getVoter().size());

        Page<AnswerDto> answerPagingDto = pagingAnswer.map(
                post -> new AnswerDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getModifyDate(),post.getAuthor().getUsername(),post.getVoter().size()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("question", questionDto);
        result.put("answers", answerPagingDto);

        return ResponseEntity.ok(result);
    }

    // 글 생성 api
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<QuestionApiForm> questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        SiteUser siteUser = userService.getUser(principal.getName());
        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        QuestionApiForm questionApiForm = new QuestionApiForm(questionForm.getSubject(), questionForm.getContent());
        return ResponseEntity.ok(questionApiForm);
    }

    // 수정 내용 조회 api
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public ResponseEntity<QuestionApiForm> questionModify(QuestionForm questionForm, @PathVariable("id") Long id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent());
        return ResponseEntity.ok(questionApiForm);
    }

    // 수정 api
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/modify/{id}")
    public ResponseEntity<QuestionApiForm> questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        QuestionApiForm questionApiForm = new QuestionApiForm(question.getSubject(), question.getContent());

        return ResponseEntity.ok(questionApiForm);
    }

    // 삭제 api
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/delete/{id}")
    public Boolean questionDelete(Principal principal, @PathVariable("id") Long id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        return this.questionService.delete(question);
    }

    // 추천 api
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public Boolean questionVote(Principal principal, @PathVariable("id") Long id) {
        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        return questionService.vote(question, siteUser);
    }


}
