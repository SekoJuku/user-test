package kz.edu.astanait.usertest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Not enough Data")
public class BadUserRequestException extends RuntimeException {
    public BadUserRequestException(String message) {
        super(message);
    }
}
