package com.mysite.sbb.lostBoard.lostApi;

import com.mysite.sbb.lostBoard.lostDto.LostSuccessDto;
import com.mysite.sbb.entity.lostEntity.LostAnswer;
import com.mysite.sbb.entity.lostEntity.LostAnswerRepository;
import com.mysite.sbb.entity.lostEntity.LostPost;
import com.mysite.sbb.lostBoard.lostForm.CreateForm;
import com.mysite.sbb.lostBoard.lostForm.LostDeleteForm;
import com.mysite.sbb.lostBoard.lostForm.ModifyForm;
import com.mysite.sbb.lostBoard.lostService.LostAnswerService;
import com.mysite.sbb.lostBoard.lostService.LostPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RequestMapping("/lost")
@RequiredArgsConstructor
@RestController
public class LostAnswerApiController {

    private final LostPostService lostPostService;
    private final LostAnswerRepository lostAnswerRepository;
    private final LostAnswerService lostAnswerService;

//    // 전체 댓글 조회 API
//    @GetMapping("/answers")
//    public List<Answer> all() {
//
//        return answerRepository.findAll();
//    }
//
//    // id로 댓글 1개 조회 API
//    @GetMapping("/answers/{id}")
//    public ResponseEntity<Answer> one(@PathVariable Long id) {
//
//        Answer answer = answerRepository.findById(id).orElse(null);
//        if (answer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");
//        return new ResponseEntity<>(answer, HttpStatus.OK);
//    }

    // 댓글 등록 API
    @PostMapping("/answers/{id}")
    public ResponseEntity<CreateForm> answerCreate(@PathVariable Long id, @Valid @RequestBody LostAnswer lostAnswerForm){

        if (lostAnswerForm.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임 입력 필수");
        }

        LostPost lostPost = this.lostPostService.getQuestion(id);
        if (lostPost == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");

        LostAnswer lostAnswer = this.lostAnswerService.create(lostPost, lostAnswerForm);
        if (lostAnswer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");

        CreateForm createForm = new CreateForm(lostAnswerForm.getContent(), lostAnswerForm.getUsername(), lostAnswer.getCreateDate());

        return (lostAnswer != null) ? ResponseEntity.status(HttpStatus.OK).body(createForm) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 댓글 수정 api
    @PutMapping("/answers/{id}")
    public ResponseEntity<ModifyForm> answerModify(@Valid @RequestBody LostAnswer newLostAnswer, @PathVariable("id") Long id) {

        LostAnswer exLostAnswer = lostAnswerRepository.findById(id).orElse(null);
        if (exLostAnswer == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다.");

        if (newLostAnswer.getPassword().equals(exLostAnswer.getPassword())) {

        return lostAnswerRepository.findById(id)
                .map(answer -> {
                    answer.setContent(newLostAnswer.getContent());
                    lostAnswerRepository.save(answer);
                    ModifyForm modifyForm = new ModifyForm(newLostAnswer.getContent(), exLostAnswer.getCreateDate());
                    return ResponseEntity.status(HttpStatus.OK).body(modifyForm);
                })
                .orElseGet(() -> {
                    newLostAnswer.setId(id);
                    lostAnswerRepository.save(newLostAnswer);
                    ModifyForm modifyForm = new ModifyForm(newLostAnswer.getContent(), exLostAnswer.getCreateDate());
                    return ResponseEntity.status(HttpStatus.OK).body(modifyForm);
                });

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 불일치");
        }
    }

    // 댓글 삭제 API
    @DeleteMapping("/answers/{id}")
    public ResponseEntity deleteComment(@PathVariable("id") Long id, @Valid @RequestBody LostDeleteForm lostDeleteForm) {
        LostAnswer lostAnswer = this.lostAnswerService.getAnswer(id);
        if (lostAnswer == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        if (lostDeleteForm.getPassword() == null || lostDeleteForm.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 입력 필수");
        }

        if (!lostAnswer.getPassword().equals(lostDeleteForm.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        LostSuccessDto lostSuccessDto = new LostSuccessDto(this.lostAnswerService.delete(lostAnswer));
        return ResponseEntity.ok(lostSuccessDto);

    }

}
