package com.mysite.sbb.meetingBoard.meetingApi.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateForm {

    private String username;
    private String content;

    public CommentCreateForm(String username, String content) {
        this.username = username;
        this.content = content;
    }

}
