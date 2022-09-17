package com.mysite.sbb.photoBoard.PhotoController.commentController;

import com.mysite.sbb.photoBoard.PhotoService.PhotoAnswerService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoCommentService;
import com.mysite.sbb.photoBoard.PhotoService.PhotoQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/photo/comment")
public class PhotoCommentController {

    private final PhotoCommentService photoCommentService;
    private final PhotoQuestionService photoQuestionService;
    private final PhotoAnswerService photoAnswerService;

    // 답변
    @GetMapping(value = "/create/answer/{id}")
    public String createAnswerComment(PhotoCommentForm photoCommentForm) {

        return "comment_form";
    }

    // 질문
    @GetMapping(value = "/create/question/{id}")
    public String createQuestionComment(PhotoCommentForm photoCommentForm) {

        return "comment_form";
    }


}
