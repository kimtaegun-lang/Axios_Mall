package org.zerock.mallapi.controller.advice;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.mallapi.util.CustomJWTException;

@RestControllerAdvice
public class CustomControllerAdvice { // 전역 예외 처리를 담당하는 클래스
    @ExceptionHandler(NoSuchElementException.class) // 값을 찾지 못했을때,
    protected ResponseEntity<?> notExist(NoSuchElementException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", msg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 잘못된 폼 데이터를 보냈을때,
    protected ResponseEntity<?> handleIllegalArgumentException(MethodArgumentNotValidException e) {
        String msg = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("msg", msg));
    }

    @ExceptionHandler(CustomJWTException.class) // CustomJWTException 예외 발생 시 실행됨.
    protected ResponseEntity<?> handleJWTException(CustomJWTException e) {

        String msg = e.getMessage();

        return ResponseEntity.ok().body(Map.of("error", msg));

    }
}
