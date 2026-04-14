package br.com.joga_together.handler;

import br.com.joga_together.dto.ErrorResponseDto;
import br.com.joga_together.exception.*;
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto>handlerBussinesException(BusinessException ex){
        ErrorResponseDto responseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "error of business",
                ex.getMessage()
        );
        return ResponseEntity.status(400).body(responseDto);
    }

    @ExceptionHandler(LoginExpiredOrBlockedException.class)
    public ResponseEntity<ErrorResponseDto>handlerLogin(LoginExpiredOrBlockedException ex){
        ErrorResponseDto responseDto = new ErrorResponseDto(
                String.valueOf(System.currentTimeMillis()),
                400,
                "error of login",
                ex.getMessage()
        );
        return ResponseEntity.status(400).body(responseDto);
    }


}
