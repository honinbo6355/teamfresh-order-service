package com.teamfresh.example.exception;

import com.teamfresh.example.api.common.BasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicResponse<Void>> handleException(Exception e) {
        return new ResponseEntity<>(new BasicResponse<>(null, new BasicResponse.BasicError(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BasicResponse<Void>> handleCustomException(CustomException e) {
        return new ResponseEntity<>(new BasicResponse<>(null, new BasicResponse.BasicError(e.getCode(), e.getMessage())), e.getStatus());
    }
}
