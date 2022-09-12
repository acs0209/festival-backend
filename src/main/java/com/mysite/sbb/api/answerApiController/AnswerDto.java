package com.mysite.sbb.api.answerApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AnswerDto {

    private Long id;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String username;

    private Integer voter;

    public AnswerDto(Long id, String content, LocalDateTime createDate,
                     LocalDateTime modifyDate, String username, Integer voter) {
        this.id = id;
        this.content = content;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.username = username;
        this.voter = voter;
    }
}
