package com.mysite.sbb.meetingBoard.api.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerModifyForm {

    private String content;

    public AnswerModifyForm(String content) {
        this.content = content;
    }
}
