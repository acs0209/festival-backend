package com.mysite.sbb.suggestionsBoard.suggestionsApi.questionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsQuestionDto {

    private Long id;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private String username;

    private Integer view;

    public SuggestionsQuestionDto(Long id, String subject, String content, LocalDateTime createDate,
                                  String username, Integer view) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.createDate = createDate;
        this.username = username;
        this.view = view;
    }
}
