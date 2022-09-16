package com.mysite.sbb.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

// 예외가 발생했을 때 API 응답으로 사용하는 객체
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
    private boolean success;
}
