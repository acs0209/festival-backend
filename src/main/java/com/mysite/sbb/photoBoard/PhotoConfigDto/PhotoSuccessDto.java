package com.mysite.sbb.photoBoard.PhotoConfigDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhotoSuccessDto {

    private Boolean success;

    public PhotoSuccessDto(Boolean success) {
        this.success = success;
    }

}
