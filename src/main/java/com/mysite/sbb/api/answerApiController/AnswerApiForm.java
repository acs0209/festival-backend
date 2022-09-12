package com.mysite.sbb.api.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerApiForm {

    private String content;

    public AnswerApiForm(String content) {
        this.content = content;
    }
}
