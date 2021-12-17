package cl.isosalud.service.controller.advice;

import cl.isosalud.service.constant.MessageConstants;
import cl.isosalud.service.dto.GenericResponseDto;
import cl.isosalud.service.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(exception);
        List<String> details = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(ex -> details.add(ex.getDefaultMessage()));
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.BAD_REQUEST.getReasonPhrase()).details(details).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = String.format(MessageConstants.X_PARAMETER_IS_MISSING, exception.getParameterName());
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.BAD_REQUEST.getReasonPhrase()).details(Collections.singletonList(detail)).build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder detail = new StringBuilder();
        detail.append(exception.getMethod()).append(MessageConstants.X_METHOD_IS_NOT_SUPPORT);
        Objects.requireNonNull(exception.getSupportedHttpMethods()).forEach(httpMethod -> detail.append(httpMethod).append(" "));
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()).details(Collections.singletonList(detail.toString().trim())).build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder detail = new StringBuilder();
        detail.append(exception.getContentType()).append(MessageConstants.X_MEDIA_TYPE_IS_NO_SUPPORT);
        exception.getSupportedMediaTypes().forEach(mediaType -> detail.append(mediaType).append(", "));
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()).details(Collections.singletonList(detail.toString())).build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        logError(exception);
        List<String> details = Collections.singletonList(String.format("%s", exception.getLocalizedMessage()));
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.NOT_FOUND.getReasonPhrase()).details(details).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> handleErrorException(GenericException exception) {
        logError(exception);
        GenericResponseDto error = GenericResponseDto.builder().message(exception.getMessage()).details(exception.getDetails()).build();
        return new ResponseEntity<>(error, exception.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        logError(exception);
        GenericResponseDto error = GenericResponseDto.builder().message(exception.getMessage()).details(Collections.singletonList(MessageConstants.UNAUTHORIZED_RESOURCE)).build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /*
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exception, WebRequest request) {
        List<String> details = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation ->
                details.add(constraintViolation.getRootBeanClass().getName() + " " + constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
        );

        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.BAD_REQUEST.getReasonPhrase()).details(details).build();
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }*/

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logError(exception);
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception) {
        logError(exception);
        GenericResponseDto error = GenericResponseDto.builder().message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception exception) {
        log.error("[ExceptionController] Excepcion capturada", exception);
    }

}