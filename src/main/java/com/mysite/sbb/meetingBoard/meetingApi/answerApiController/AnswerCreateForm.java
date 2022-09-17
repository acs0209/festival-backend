package com.mysite.sbb.meetingBoard.meetingApi.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnswerCreateForm {

    private String content;
    private String username;
    private String createDate;

    public AnswerCreateForm(String content, String username, String createDate) {
        this.content = content;
        this.username = username;
        this.createDate = createDate;
    }

}
