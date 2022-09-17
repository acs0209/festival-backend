package com.mysite.sbb.suggestionsBoard.suggestionsApi.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsAnswerModifyForm {

    private String content;

    public SuggestionsAnswerModifyForm(String content) {
        this.content = content;
    }
}
