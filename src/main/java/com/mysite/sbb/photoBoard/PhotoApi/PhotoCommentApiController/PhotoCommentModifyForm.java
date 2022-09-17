package com.mysite.sbb.photoBoard.PhotoApi.PhotoCommentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoCommentModifyForm {

    private String content;

    public PhotoCommentModifyForm(String content) {
        this.content = content;
    }

}
