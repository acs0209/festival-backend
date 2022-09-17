package com.mysite.sbb.suggestionsBoard.suggestionsConfigDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuggestionsSuccessDto {

    private Boolean success;

    public SuggestionsSuccessDto(Boolean success) {
        this.success = success;
    }

}
