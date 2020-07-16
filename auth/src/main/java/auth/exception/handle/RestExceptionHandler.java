package auth.exception.handle;

import auth.exception.TokenException;
import auth.exception.UserAlreadyExistException;
import auth.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                ex.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    protected ResponseEntity<Object> userAlreadyExistExceptionHandle() {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                "User already exists."
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {TokenException.class})
    protected ResponseEntity<Object> tokenExceptionHandle() {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                "Invalid or expired token."
        ), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> userNotFoundExceptionHandle() {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                "User doesn't exists."
        ), HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class ApiExceptionResponseEntity {

        private Date timestamp;

        private String message;
    }
}
