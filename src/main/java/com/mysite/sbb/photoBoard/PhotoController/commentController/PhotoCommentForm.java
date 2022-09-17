package com.mysite.sbb.photoBoard.PhotoController.commentController;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PhotoCommentForm {

    @NotEmpty(message = "작성자는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

}
