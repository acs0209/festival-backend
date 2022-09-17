package com.mysite.sbb.suggestionsBoard.suggestionsController.answerController;

import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suggestions/answer")
@RequiredArgsConstructor
public class SuggestionsAnswerController {

    private final SuggestionsAnswerService suggestionsAnswerService;

//    @GetMapping("/vote/{id}")
//    public String answerVote(Principal principal, @PathVariable("id") Long id) {
//        Answer answer = this.answerService.getAnswer(id);
//        String siteUser = answer.getUsername();
//        this.answerService.vote(answer, siteUser);
//        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
//    }

}
