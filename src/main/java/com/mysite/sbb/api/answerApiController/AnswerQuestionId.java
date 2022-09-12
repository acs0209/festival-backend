package com.mysite.sbb.api.answerApiController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerQuestionId {

    private Long answerQuestionId;
    private Long answerId;

    public AnswerQuestionId(Long answerQuestionId, Long answerId) {
        this.answerQuestionId = answerQuestionId;
        this.answerId = answerId;
    }

}
