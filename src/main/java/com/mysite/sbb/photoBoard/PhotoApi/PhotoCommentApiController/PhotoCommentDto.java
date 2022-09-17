package com.mysite.sbb.photoBoard.PhotoApi.PhotoCommentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoCommentDto {

    private Long id;

    private String content;

    private String date;

    private String username;


    public PhotoCommentDto(Long id, String content, String createDate, String username) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.username = username;
    }
}
