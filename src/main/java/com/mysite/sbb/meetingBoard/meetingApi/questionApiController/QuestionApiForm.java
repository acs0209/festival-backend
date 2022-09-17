package com.mysite.sbb.meetingBoard.meetingApi.questionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionApiForm {

    private String subject;
    private String content;
    private String username;

    public QuestionApiForm(String subject, String content, String username) {
        this.subject = subject;
        this.content = content;
        this.username = username;
    }

}