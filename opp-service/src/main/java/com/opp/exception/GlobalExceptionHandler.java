package com.opp.exception;

import com.opp.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler that intercepts all exceptions. Stack traces should not be exposed externally.

 * Created by ctobe on 6/24/16.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return errorResponse(ex, HttpStatus.BAD_REQUEST, request, messageForMethodArgumentNotValidException(ex));
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException exception, WebRequest webRequest) {
        return errorResponse(exception, HttpStatus.NOT_FOUND, webRequest, "Resource not found: " + exception.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequest(BadRequestException exception, WebRequest webRequest) {
        return errorResponse(exception, HttpStatus.BAD_REQUEST, webRequest, exception.getMessage());
    }

    @ExceptionHandler({InternalServiceException.class, Exception.class})
    public ResponseEntity<Object> handleInternalError(Exception exception) {
        LOG.error("Caught unexpected exception", exception);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred when servicing your request. Please try again."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> errorResponse(Exception e, HttpStatus httpStatus, WebRequest webRequest, String message) {
        return errorResponse(e, httpStatus, webRequest, ErrorResponse.Type.GENERAL, message);
    }

    private ResponseEntity<Object> errorResponse(Exception e, HttpStatus httpStatus, WebRequest webRequest, ErrorResponse.Type type, String message) {
        LOG.info("Caught expected exception", e);
        ErrorResponse body = new ErrorResponse(httpStatus.value(), type, message);
        return handleExceptionInternal(e, body, new HttpHeaders(), httpStatus, webRequest);
    }

    private String messageForMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder("Validation failed: ");
        List<String> validationErrorMessages = new LinkedList<>();
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            validationErrorMessages.add(error.getDefaultMessage());
        }
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrorMessages.add(String.format("'%s' %s", error.getField(), error.getDefaultMessage()));
        }
        sb.append(validationErrorMessages.stream().collect(Collectors.joining(", ")));
        sb.append(".");
        return sb.toString();
    }



}
