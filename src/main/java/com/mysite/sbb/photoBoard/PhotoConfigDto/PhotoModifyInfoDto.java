package com.mysite.sbb.photoBoard.PhotoConfigDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class PhotoModifyInfoDto {

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    private String password;

    public PhotoModifyInfoDto(String content, String password) {
        this.content = content;
        this.password = password;
    }
}
