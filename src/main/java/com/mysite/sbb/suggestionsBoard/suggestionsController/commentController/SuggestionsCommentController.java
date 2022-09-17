package com.mysite.sbb.suggestionsBoard.suggestionsController.commentController;

import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsAnswerService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsCommentService;
import com.mysite.sbb.suggestionsBoard.suggestionsService.SuggestionsQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/suggestions/comment")
public class SuggestionsCommentController {

    private final SuggestionsCommentService suggestionsCommentService;
    private final SuggestionsQuestionService suggestionsQuestionService;
    private final SuggestionsAnswerService suggestionsAnswerService;

    // 답변
    @GetMapping(value = "/create/answer/{id}")
    public String createAnswerComment(SuggestionsCommentForm suggestionsCommentForm) {

        return "comment_form";
    }

    // 질문
    @GetMapping(value = "/create/question/{id}")
    public String createQuestionComment(SuggestionsCommentForm suggestionsCommentForm) {

        return "comment_form";
    }


}
