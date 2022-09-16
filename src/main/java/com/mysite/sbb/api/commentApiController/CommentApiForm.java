package com.mysite.sbb.api.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentApiForm {

    private String content;

    public CommentApiForm(String content) {
        this.content = content;
    }

}
