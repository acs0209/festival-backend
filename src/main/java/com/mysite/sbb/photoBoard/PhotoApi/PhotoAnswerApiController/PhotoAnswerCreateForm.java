package com.mysite.sbb.photoBoard.PhotoApi.PhotoAnswerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoAnswerCreateForm {

    private String content;
    private String username;

    public PhotoAnswerCreateForm(String content, String username){
        this.content = content;
        this.username = username;
    }

}
