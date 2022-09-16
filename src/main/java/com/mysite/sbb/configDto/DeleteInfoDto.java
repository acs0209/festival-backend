package com.mysite.sbb.configDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeleteInfoDto {


    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    private String password;

    public DeleteInfoDto(String password) {
        this.password = password;
    }
}
