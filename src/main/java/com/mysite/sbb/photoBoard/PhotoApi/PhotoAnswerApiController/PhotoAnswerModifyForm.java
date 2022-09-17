package com.mysite.sbb.photoBoard.PhotoApi.PhotoAnswerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoAnswerModifyForm {

    private String content;

    public PhotoAnswerModifyForm(String content) {
        this.content = content;
    }
}
