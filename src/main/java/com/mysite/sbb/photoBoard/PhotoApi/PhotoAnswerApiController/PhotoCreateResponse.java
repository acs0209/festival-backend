package com.mysite.sbb.photoBoard.PhotoApi.PhotoAnswerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoCreateResponse {

    private String content;
    private String username;
    private String createDate;

    public PhotoCreateResponse(String content, String username, String createDate) {
        this.content = content;
        this.username = username;
        this.createDate = createDate;
    }

}
