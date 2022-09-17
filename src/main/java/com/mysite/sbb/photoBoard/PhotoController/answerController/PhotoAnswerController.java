package com.mysite.sbb.photoBoard.PhotoController.answerController;

import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photo/answer")
@RequiredArgsConstructor
public class PhotoAnswerController {

    private final PhotoAnswerService photoAnswerService;

//    @GetMapping("/vote/{id}")
//    public String answerVote(Principal principal, @PathVariable("id") Long id) {
//        Answer answer = this.answerService.getAnswer(id);
//        String siteUser = answer.getUsername();
//        this.answerService.vote(answer, siteUser);
//        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
//    }

}
