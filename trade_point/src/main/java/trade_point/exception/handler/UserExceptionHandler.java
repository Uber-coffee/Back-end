package trade_point.exception.handler;

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
import trade_point.exception.TradePointNotFoundException;
import trade_point.exception.UserNotFoundException;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class ApiExceptionResponseEntity {

        private Date timestamp;

        private String message;
    }

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

    @ExceptionHandler(value = {TradePointNotFoundException.class})
    protected ResponseEntity<Object> TradePointNotFoundExceptionHandle() {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                "Trade point doesn't exists."
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> UserNotFoundExceptionHandle() {
        return new ResponseEntity<>(new ApiExceptionResponseEntity(
                new Date(),
                "User doesn't exists."
        ), HttpStatus.NOT_FOUND);
    }
}
