package com.mysite.sbb.meetingBoard.meetingController.answerController;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    @NotEmpty(message = "사용자 이름은 필수 항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    private String password;


}
