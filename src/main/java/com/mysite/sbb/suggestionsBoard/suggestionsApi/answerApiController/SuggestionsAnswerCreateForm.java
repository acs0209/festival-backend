package com.mysite.sbb.suggestionsBoard.suggestionsApi.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsAnswerCreateForm {

    private String content;
    private String username;

    public SuggestionsAnswerCreateForm(String content, String username) {
        this.content = content;
        this.username = username;
    }

}
