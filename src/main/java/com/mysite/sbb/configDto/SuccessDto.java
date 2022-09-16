package com.mysite.sbb.configDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessDto {

    private Boolean success;

    public SuccessDto(Boolean success) {
        this.success = success;
    }

}
