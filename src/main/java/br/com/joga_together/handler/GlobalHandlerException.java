package br.com.joga_together.handler;

import br.com.joga_together.dto.ErrorResponseDto;
import br.com.joga_together.exception.GameAlreadyExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "Bad Request",
                details
        );
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "Bad Request",
                details
        );
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(GameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto>handlerExceptionGameAlreadyExistsException(GameAlreadyExistsException ex){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}
