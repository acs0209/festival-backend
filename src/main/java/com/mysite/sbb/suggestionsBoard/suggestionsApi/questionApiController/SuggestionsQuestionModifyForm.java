package com.mysite.sbb.suggestionsBoard.suggestionsApi.questionApiController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuggestionsQuestionModifyForm {

    private String subject;

    private String content;

    public SuggestionsQuestionModifyForm(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}
