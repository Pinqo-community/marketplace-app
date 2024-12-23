package com.marketplace.exception;

import com.marketplace.dto.ExceptionResponse;
import com.marketplace.model.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Validation Error",
                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                        ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> new ErrorDetail(
                                        fieldError.getField(),
                                        fieldError.getDefaultMessage()
                                ))
                                .collect(Collectors.toList())
                )
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {
        String paramName = ex.getParameterName();
        String errorMessage = String.format("Le paramètre '%s' est requis et ne peut pas être vide.", paramName);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage,
                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                        List.of(new ErrorDetail(paramName, "Paramètre manquant"))
                )
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String paramName = ex.getPropertyName();
        String errorMessage = String.format("Le paramètre '%s' doit être un booléen valide.", paramName);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMessage,
                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                        List.of(new ErrorDetail(paramName, "Type incorrect"))
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    @ExceptionHandler({WrongCredentialException.class, InvalidTokenException.class})
    public ResponseEntity<ExceptionResponse> handleWrongCredentialException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    @ExceptionHandler(UnavailableProductException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity<ExceptionResponse>handleUnavailableProductException(UnavailableProductException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.GONE).body(
                new ExceptionResponse(
                        HttpStatus.GONE.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse>handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleInternalException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage().isEmpty() ? "Internal Error" : ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }
}
