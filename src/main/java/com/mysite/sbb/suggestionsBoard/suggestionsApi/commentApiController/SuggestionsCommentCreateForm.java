package com.mysite.sbb.suggestionsBoard.suggestionsApi.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsCommentCreateForm {

    private String username;
    private String content;

    public SuggestionsCommentCreateForm(String username, String content) {
        this.username = username;
        this.content = content;
    }

}
