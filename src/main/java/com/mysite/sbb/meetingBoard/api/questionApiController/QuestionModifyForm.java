package com.mysite.sbb.meetingBoard.api.questionApiController;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class QuestionModifyForm {

    private String subject;

    private String content;

    public QuestionModifyForm(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}
