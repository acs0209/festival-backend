package com.mysite.sbb.photoBoard.PhotoApi.PhotoCommentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoCommentCreateForm {

    private String username;

    private String content;

    private String createDate;

    public PhotoCommentCreateForm(String username, String content, String createDate){
        this.username = username;
        this.content = content;
        this.createDate = createDate;
    }

}
