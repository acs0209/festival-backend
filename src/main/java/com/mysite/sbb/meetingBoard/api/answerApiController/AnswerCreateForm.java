package com.mysite.sbb.meetingBoard.api.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateForm {

    private String content;
    private String username;

    public AnswerCreateForm(String content, String username) {
        this.content = content;
        this.username = username;
    }

}
