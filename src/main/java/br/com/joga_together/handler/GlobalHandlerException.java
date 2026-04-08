package br.com.joga_together.handler;

import br.com.joga_together.dto.ErrorResponseDto;
import br.com.joga_together.exception.CodeInvalidOrExpireException;
import br.com.joga_together.exception.GameAlreadyExistsException;
import br.com.joga_together.exception.UserAlreadyExistsException;
import br.com.joga_together.exception.UserAlreadyInGroupException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
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

    @ExceptionHandler(UserAlreadyInGroupException.class)
    public ResponseEntity<ErrorResponseDto>handlerExceptionUserAlreadyInGroup(UserAlreadyInGroupException ex){
        ErrorResponseDto responseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                409,
                "Conflict",
                ex.getMessage()
        );
        return ResponseEntity.status(409).body(responseDto);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto>handlerExceptionUserExistis(UserAlreadyExistsException ex){
        ErrorResponseDto responseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                409,
                "Conflict",
                ex.getMessage()
        );
        return ResponseEntity.status(409).body(responseDto);
    }

    @ExceptionHandler(CodeInvalidOrExpireException.class)
    public ResponseEntity<ErrorResponseDto>handlerCodeInvalid(CodeInvalidOrExpireException ex){
        ErrorResponseDto responseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "code invalid",
                ex.getMessage()
        );
        return ResponseEntity.status(400).body(responseDto);
    }


}
