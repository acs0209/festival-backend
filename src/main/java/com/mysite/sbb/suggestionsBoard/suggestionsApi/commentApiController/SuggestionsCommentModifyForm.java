package com.mysite.sbb.suggestionsBoard.suggestionsApi.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsCommentModifyForm {

    private String content;

    public SuggestionsCommentModifyForm(String content) {
        this.content = content;
    }

}
