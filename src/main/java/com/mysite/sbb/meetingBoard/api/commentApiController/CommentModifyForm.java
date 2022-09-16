package com.mysite.sbb.meetingBoard.api.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentModifyForm {

    private String content;

    public CommentModifyForm(String content) {
        this.content = content;
    }

}
