package com.mysite.sbb.api.answerApiController;

import com.mysite.sbb.entity.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private String username;

    private List<Comment> commentList;


    public AnswerDto(Long id, String content, LocalDateTime createDate,
                            String username, List<Comment> commentList) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.username = username;
        this.commentList = commentList;
    }
}
