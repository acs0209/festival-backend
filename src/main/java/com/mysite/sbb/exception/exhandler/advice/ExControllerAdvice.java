package com.mysite.sbb.exception.exhandler.advice;

import com.mysite.sbb.exception.exception.DataNotFoundException;
import com.mysite.sbb.exception.exception.UserException;
import com.mysite.sbb.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage(), false);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage(), false);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> responseExHandle(ResponseStatusException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage(), false);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> dataNotExHandle(DataNotFoundException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage(), false);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }


    /*
     * ExceptionHandler 는 같은 컨트롤러 안에서만 적용 된다 다른 컨트롤러는 상관이 없다
     * 같이 속해 있는 컨트롤러 안에서 발생한 예외만 처리해준다
     *
     * Exception 는 위에 있는 ExceptionHandler 에서 처리하지 못한 예외는 Exception 으로 온다
     * 실수로 놓치 예외 혹은 공통으로 처리할 예외를 주로 처리한다
     * */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류", false);
    }

}