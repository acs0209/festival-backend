package com.mysite.sbb.api.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentApiForm {

    private String content;
    private Long questionId;

    public CommentApiForm(String content, Long questionId) {
        this.content = content;
        this.questionId = questionId;
    }

}
