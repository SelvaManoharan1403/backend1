package com.profitableaccountingsystemapi.exception;

import com.profitableaccountingsystemapi.common.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        APIResponse apiResponse = new APIResponse();
        //HttpStatus.UNAUTHORIZED.value()
        apiResponse.setStatus(403);

        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
