package com.mysite.sbb.api.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String username;


    public CommentDto(Long id, String content, LocalDateTime createDate, LocalDateTime modifyDate, String username) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.username = username;
    }
}
