package com.mysite.sbb.suggestionsBoard.suggestionsApi.commentApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsCommentDto {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private String username;


    public SuggestionsCommentDto(Long id, String content, LocalDateTime createDate, String username) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.username = username;
    }
}
