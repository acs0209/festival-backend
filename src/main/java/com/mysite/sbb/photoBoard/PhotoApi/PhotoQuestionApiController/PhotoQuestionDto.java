package com.mysite.sbb.photoBoard.PhotoApi.PhotoQuestionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoQuestionDto {

    private Long id;

    private String subject;

    private String content;

    private String date;

    private String username;

    private String filename;

    private String filepath;
    private Integer view;

    private Integer voter;

    public PhotoQuestionDto(Long id, String subject, String content, String date,
                            String username, Integer view, String filename, String filepath) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.username = username;
        this.view = view;
        this.filename = filename;

        this.filepath = filepath;
    }
}