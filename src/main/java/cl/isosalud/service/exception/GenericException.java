package cl.isosalud.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class GenericException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final List<String> details;

    public GenericException(HttpStatus httpStatus, String message, List<String> details) {
        super(message);
        this.httpStatus = httpStatus;
        this.details = details;
    }

    public GenericException(String message, List<String> details) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.details = details;
    }

    public GenericException(HttpStatus httpStatus, String message, String details) {
        super(message);
        this.httpStatus = httpStatus;
        this.details = Collections.singletonList(details);
    }

}