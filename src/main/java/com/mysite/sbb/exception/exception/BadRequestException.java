package com.mysite.sbb.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// 월래 500으로 처리되는데 ResponseStatus 어노테이션을 통해 자기가 지정할 수 있다
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") // BAD_REQUEST -> 400
public class BadRequestException extends RuntimeException {

}