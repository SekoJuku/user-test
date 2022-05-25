package kz.edu.astanait.usertest.exception;

import kz.edu.astanait.usertest.exception.domain.BadUserRequestException;
import kz.edu.astanait.usertest.exception.domain.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class CustomExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<HttpResponseException> userNotFoundException(UserNotFoundException exception) {
        log.debug(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    @ExceptionHandler(BadUserRequestException.class)
    public ResponseEntity<HttpResponseException> badUserRequestException(BadUserRequestException exception) {
        log.debug(exception.getMessage());
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ResponseEntity<HttpResponseException> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponseException(httpStatus.value(), httpStatus,
            httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}

