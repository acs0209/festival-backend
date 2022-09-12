package com.mysite.sbb.api.questionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionApiForm {

    private String subject;
    private String content;

    public QuestionApiForm(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}