package com.mysite.sbb.photoBoard.PhotoApi.PhotoQuestionApiController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoQuestionApiForm {


    private String subject;

    private String content;

    private String username;

    private String filename;

    private String filepath;

    private String createDate;

    public PhotoQuestionApiForm(String subject, String content, String username, String filename, String filepath, String createDate) {
        this.subject = subject;
        this.content = content;
        this.username = username;
        this.filename = filename;
        this.filepath = filepath;
        this.createDate = createDate;
    }

}
