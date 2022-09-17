package com.mysite.sbb.lostBoard.lostApi;

import com.mysite.sbb.lostBoard.lostDto.LostAnswerDto;
import com.mysite.sbb.lostBoard.lostDto.LostCommentDto;
import com.mysite.sbb.lostBoard.lostDto.LostPostDto;
import com.mysite.sbb.lostBoard.lostDto.LostSuccessDto;
import com.mysite.sbb.entity.lostEntity.LostAnswer;
import com.mysite.sbb.entity.lostEntity.LostComment;
import com.mysite.sbb.entity.lostEntity.LostPost;
import com.mysite.sbb.entity.lostEntity.LostPostRepository;
import com.mysite.sbb.lostBoard.lostForm.LostDeleteForm;
import com.mysite.sbb.lostBoard.lostService.LostAnswerService;
import com.mysite.sbb.lostBoard.lostService.LostCommentService;
import com.mysite.sbb.lostBoard.lostService.LostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lost")
class LostPostApiController {

    @Autowired
    private LostPostRepository repository;

    @Autowired
    private LostPostService lostPostService;

    @Autowired
    private LostAnswerService lostAnswerService;

    @Autowired
    private LostCommentService lostCommentService;

    // 페이징, 검색(제목, 내용에 포함) 조회 API
    @GetMapping("/posts")
    public Page<LostPost> list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam(required = false, defaultValue = "") String searchText) {

        return repository.findBySubjectContainingOrContentContaining(searchText, searchText, pageable);
    }

    /*
    * 상세 질문을 보기 위해 데이터를 가공하는 함수
      답변 페이징 처리를 위해 @RequestParam(value = "page", defaultValue = "0") int page 추가
    * */
    @GetMapping("/posts/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable("id") Long id,
                                                      @RequestParam(value = "page", defaultValue = "0") int page) {

        // 답변 페이징 처리
        Page<LostAnswer> pagingAnswer = lostAnswerService.getList(page, id);
        LostPost lostPost = this.lostPostService.getQuestion(id);
        Page<LostComment> commentPage = lostCommentService.getLostPostCommentList(page, id);

        if (pagingAnswer.getNumberOfElements() == 0 && page != 0) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        LostPostDto lostPostDto = new LostPostDto(lostPost.getId(), lostPost.getSubject(), lostPost.getContent(),
                lostPost.getCreateDate(), lostPost.getUsername(), lostPost.getIsLost());
        
        Page<LostAnswerDto> answerPagingDto = pagingAnswer.map(
                post -> new LostAnswerDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername(),
                        post.getLostCommentList()
                ));
        Page<LostCommentDto> commentDtoPage = commentPage.map(
                post -> new LostCommentDto(
                        post.getId(),post.getContent(),post.getCreateDate(),
                        post.getUsername()
                ));

        Map<String, Object> result = new HashMap<>();
        result.put("questions", lostPostDto);
        result.put("answers", answerPagingDto);

        return ResponseEntity.ok(result);
    }

    // 글 작성 API
    @PostMapping("/posts")
    LostPost newQuestion(@Valid LostPost newLostPost, MultipartFile file, BindingResult bindingResult, HttpServletRequest request) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        if (newLostPost.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임 입력 필수");
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        newLostPost.setCreateDate(now);

        if (file == null) {
            repository.save(newLostPost);
        } else {
            lostPostService.write(newLostPost, file, request);
        }

        return repository.findById(newLostPost.getId()).orElse(null);
    }

    // 글 수정 API
    @PutMapping("/posts/{id}")
    ResponseEntity<LostPost> replaceQuestion(@Valid LostPost newLostPost, @PathVariable Long id, MultipartFile file, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 입력 값입니다.");
        }

        LostPost exLostPost = repository.findById(id).orElse(null);
        if (exLostPost == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");

        // 새로운 파일경로
        String filePath = lostPostService.getFilePath(newLostPost);

        // 삭제할 파일경로
        String deleteFilePath = lostPostService.getFilePath(exLostPost);

        if (newLostPost.getPassword().equals(exLostPost.getPassword())) {

            return repository.findById(id)
                    .map(question -> {
                        question.setSubject(newLostPost.getSubject());
                        question.setContent(newLostPost.getContent());
                        question.setIsLost(newLostPost.getIsLost());

                        if (file == null) {
                            lostPostService.deleteFile(question);
                            repository.save(question);
                            return ResponseEntity.status(HttpStatus.OK).body(question);
                        }

                        if (file.isEmpty()) {
                            lostPostService.deleteFile(question);
                            repository.save(question);
                            return ResponseEntity.status(HttpStatus.OK).body(question);
                        } else {
                            try {
                                // 파일을 수정할 경우 기존 파일 제거
                                if (!filePath.equals(deleteFilePath)) {
                                    File deleteFile = new File(deleteFilePath);
                                    deleteFile.delete();
                                }

                                lostPostService.write(question, file, request);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        LostPost resultLostPost = repository.findById(question.getId()).orElse(null);
                        return ResponseEntity.status(HttpStatus.OK).body(resultLostPost);
                    })
                    .orElseGet(() -> {
                        newLostPost.setId(id);
                        repository.save(newLostPost);
                        return ResponseEntity.status(HttpStatus.OK).body(newLostPost);
                    });
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
        }
    }

//    // 파일 삭제 API
//    @DeleteMapping("/questions/files/{id}")
//    void deleteFile(@PathVariable Long id) {
//
//        Question question = repository.findById(id).orElse(null);
//
//        questionService.deleteFile(question);
//        repository.save(question);
//    }

    // 글 삭제 API
    @DeleteMapping("/posts/{id}")
    ResponseEntity deleteQuestion(@PathVariable Long id, @Valid @RequestBody LostDeleteForm lostDeleteForm) {

        LostPost lostPost = repository.findById(id).orElse(null);

        if (lostPost == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");

        if (lostDeleteForm.getPassword() == null || lostDeleteForm.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 입력 필수");
        }

        if (lostDeleteForm.getPassword().equals(lostPost.getPassword())) {
            LostSuccessDto lostSuccessDto = new LostSuccessDto(this.lostPostService.delete(lostPost));
            lostPostService.deleteFile(lostPost);
            return ResponseEntity.ok(lostSuccessDto);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
    }

}