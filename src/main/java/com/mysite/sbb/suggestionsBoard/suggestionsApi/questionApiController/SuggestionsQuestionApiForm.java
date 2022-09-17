package com.mysite.sbb.suggestionsBoard.suggestionsApi.questionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsQuestionApiForm {

    private String subject;
    private String content;
    private String username;

    public SuggestionsQuestionApiForm(String subject, String content, String username) {
        this.subject = subject;
        this.content = content;
        this.username = username;
    }

}