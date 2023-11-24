package com.isc.authentication.utils.errorhandling;

import com.isc.authentication.utils.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @PostConstruct
    public void init() {
        Logger.getLogger(GenericExceptionHandler.class.getCanonicalName()).log(Level.INFO, "Generic Exception Handling active");
    }

    @ResponseBody
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, UnauthenticatedException.class})
    public ResponseEntity<ResponseError> handleAuthenticationError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ResponseError> handleResourceNotFoundError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class, CredentialsExpiredException.class})
    public ResponseEntity<ResponseError> handleUnauthorizedError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ResponseBody
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseError> handleValidationError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseError body = new ResponseError();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            body.setMessage(StringUtils.isBlank(body.getMessage())
                    ? error.getDefaultMessage()
                    : body.getMessage() + ", " + error.getDefaultMessage());
        });
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler({UnsupportedOperationException.class})
    public ResponseEntity<ResponseError> handleUnsupportedOperationError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_IMPLEMENTED);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseError> handleGerericError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        Logger.getLogger(GenericExceptionHandler.class.getSimpleName()).log(Level.SEVERE, ExceptionUtils.getStackTrace(ex));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({EmailException.class})
    public ResponseEntity<ResponseError> handleEmailError(Exception ex) {
        ResponseError body = new ResponseError(ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
