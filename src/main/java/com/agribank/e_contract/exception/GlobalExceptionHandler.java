//package com.agribank.e_contract.exception;
//
//import com.agribank.e_contract.response.CommonResponse;
//import jakarta.validation.ConstraintViolationException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<CommonResponse> handleNotFound(ResourceNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new CommonResponse("404", ex.getMessage()));
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<CommonResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new CommonResponse("400", ex.getMessage()));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CommonResponse> handleValidation(MethodArgumentNotValidException ex) {
//        String msg = ex.getBindingResult().getFieldErrors().stream()
//                .findFirst()
//                .map(err -> err.getField() + ": " + err.getDefaultMessage())
//                .orElse("Validation failed");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new CommonResponse("400", msg));
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<CommonResponse> handleConstraint(ConstraintViolationException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(new CommonResponse("400", ex.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CommonResponse> handleException(Exception ex) {
//        // log.error("Unhandled exception", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new CommonResponse("500", "Internal Server Error"));
//    }
//}