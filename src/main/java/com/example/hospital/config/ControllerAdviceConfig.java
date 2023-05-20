package com.example.hospital.config;

import com.example.hospital.exception.OptionalObjectNullException;
import com.example.hospital.model.response.CommonErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ControllerAdviceConfig {
    // Null pointer exception 처리
    @ExceptionHandler(OptionalObjectNullException.class)
    protected ResponseEntity<CommonErrorResponse> handleOptionalObjectNullException(OptionalObjectNullException e, WebRequest webRequest) {
        log.error("OptionalObjectNullException", e);
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if (body != null) {
            log.info("handleOptionalObjectNullException param={}", body);
        }
        CommonErrorResponse errorResponse = new CommonErrorResponse("Parameter error");
        errorResponse.setResultCode(99);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ConstraintViolationException 처리
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CommonErrorResponse> handleConstraintViolationException(ConstraintViolationException e, WebRequest webRequest) {
        log.error("ConstraintViolationException", e);
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if (body != null) {
            log.info("ConstraintViolationException param={}", body);
        }
        CommonErrorResponse errorResponse = new CommonErrorResponse(e.getMessage());
        errorResponse.setResultCode(98);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<CommonErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, WebRequest webRequest) {
        log.error("IllegalArgumentException", e);
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if (body != null) {
            log.info("IllegalArgumentException param={}", body);
        }
        CommonErrorResponse errorResponse = new CommonErrorResponse(e.getMessage());
        errorResponse.setResultCode(98);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest webRequest) {
        log.error("MethodArgumentNotValidException", e);
        Object body = webRequest.getAttribute("body", RequestAttributes.SCOPE_REQUEST);
        if (body != null) {
            log.info("MethodArgumentNotValidException param={}", body);
        }
        CommonErrorResponse errorResponse = new CommonErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        errorResponse.setResultCode(98);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
