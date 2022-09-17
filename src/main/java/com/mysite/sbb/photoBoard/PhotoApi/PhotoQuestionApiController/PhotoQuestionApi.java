package com.mysite.sbb.photoBoard.PhotoApi.PhotoQuestionApiController;

import com.mysite.sbb.photoBoard.PhotoApi.PhotoAnswerApiController.PhotoAnswerDto;
import com.mysite.sbb.photoBoard.PhotoApi.PhotoCommentApiController.PhotoCommentDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoDeleteInfoDto;
import com.mysite.sbb.photoBoard.PhotoConfigDto.PhotoSuccessDto;
import com.mysite.sbb.photoBoard.PhotoController.questionController.PhotoQuestionForm;
import com.mysite.sbb.entity.PhotoEntity.photoanswer.PhotoAnswer;
import com.mysite.sbb.entity.PhotoEntity.photocomment.PhotoComment;
import com.mysite.sbb.entity.PhotoEntity.photoquestion.PhotoQuestion;
import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoCommentService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/photo/post")
@RequiredArgsConstructor
public class PhotoQuestionApi {

    private final PhotoQuestionService photoQuestionService;
    private final PhotoAnswerService photoAnswerService;
    private final PhotoCommentService photoCommentService;


    // 게시판 전체 불러오는 것
    @GetMapping("/list")
    public ResponseEntity<Page<PhotoQuestionDto>> list(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<PhotoQuestion> paging = photoQuestionService.getList(page, kw);


        if (paging.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        Page<PhotoQuestionDto> questionDtoPage = paging.map(post -> new PhotoQuestionDto(post.getId(), post.getSubject(), post.getContent(), post.getDate(), post.getUsername(), post.getView(), post.getFilename(), post.getFilepath()));

        return ResponseEntity.ok(questionDtoPage);
    }

    /*
     * 상세 질문을 보기 위해 데이터를 가공하는 함수
       답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
     * */
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable("id") Long id, @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<PhotoAnswer> pagingAnswer = photoAnswerService.getList(page, id);
        PhotoQuestion photoQuestion = this.photoQuestionService.getQuestion(id);
        Page<PhotoComment> commentPage = photoCommentService.getQuestionCommentList(page, id);

        if (pagingAnswer.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        photoQuestionService.updateView(id); // views ++ 조회수 처리
        PhotoQuestionDto photoQuestionDto = new PhotoQuestionDto(photoQuestion.getId(), photoQuestion.getSubject(), photoQuestion.getContent(), photoQuestion.getDate(), photoQuestion.getUsername(), photoQuestion.getView(), photoQuestion.getFilename(), photoQuestion.getFilepath());

        Page<PhotoAnswerDto> answerPagingDto = pagingAnswer.map(post -> new PhotoAnswerDto(post.getId(), post.getContent(), post.getDate(), post.getUsername(), post.getPhotoCommentList()));
        Page<PhotoCommentDto> commentDtoPage = commentPage.map(post -> new PhotoCommentDto(post.getId(), post.getContent(), post.getDate(), post.getUsername()));

        Map<String, Object> result = new HashMap<>();
        result.put("photoQuestion", photoQuestionDto);
        result.put("answers", answerPagingDto);
        result.put("questionComments", commentDtoPage);

        return ResponseEntity.ok(result);
    }

    // 글 생성 api --> 사진 1장만 가능
    @PostMapping("/")
    public ResponseEntity<PhotoQuestionApiForm> questionCreate(@Valid PhotoQuestionForm photoQuestionForm, BindingResult bindingResult, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        PhotoQuestion q = photoQuestionService.create(photoQuestionForm, file);

        PhotoQuestionApiForm photoQuestionApiForm = new PhotoQuestionApiForm(q.getSubject(), q.getContent(), q.getUsername(), q.getFilename(), q.getFilepath(), q.getDate());
        return ResponseEntity.ok(photoQuestionApiForm);
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
    public ResponseEntity<PhotoQuestionModifyForm> questionModify(@Valid PhotoQuestionForm photoQuestionForm, BindingResult bindingResult, @PathVariable("id") Long id, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

      PhotoQuestion photoQuestion = this.photoQuestionService.getQuestion(id);

        if (!photoQuestion.getPassword().equals(photoQuestionForm.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
        }

        this.photoQuestionService.modify(photoQuestion, photoQuestionForm.getSubject(), photoQuestionForm.getContent(), file);

       PhotoQuestionModifyForm photoQuestionModifyForm = new PhotoQuestionModifyForm(photoQuestion.getSubject(), photoQuestion.getContent(), photoQuestion.getFilename(), photoQuestion.getFilepath());
        return ResponseEntity.ok(photoQuestionModifyForm);
    }

    // 삭제 api --> form-date로 보내야 한다.
    @DeleteMapping("/{id}")
    public ResponseEntity<PhotoSuccessDto> questionDelete(@Valid PhotoDeleteInfoDto photoDeleteInfoDto, @PathVariable("id") Long id) {
        PhotoQuestion photoQuestion = this.photoQuestionService.getQuestion(id);

        if (!photoQuestion.getPassword().equals(photoDeleteInfoDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }

        PhotoSuccessDto photoSuccessDto = new PhotoSuccessDto(this.photoQuestionService.delete(photoQuestion));
        return ResponseEntity.ok(photoSuccessDto);
    }
}
