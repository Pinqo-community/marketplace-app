package com.marketplace.web.exception;

import com.marketplace.api.dto.exception.ErrorDetail;
import com.marketplace.api.dto.exception.ExceptionResponse;
import com.marketplace.api.exception.*;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from request body and parameters.
     *
     * @param ex the validation exception
     * @param request the current request
     * @return ResponseEntity with BAD_REQUEST status and validation error details
     */
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

    /**
     * Handles missing required request parameters.
     *
     * @param ex the missing parameter exception
     * @param request the current request
     * @return ResponseEntity with BAD_REQUEST status and missing parameter details
     */
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

    /**
     * Handles type mismatch in request parameters.
     *
     * @param ex the type mismatch exception
     * @param request the current request
     * @return ResponseEntity with BAD_REQUEST status and type mismatch details
     */
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

    /**
     * Handles authentication and token validation errors.
     *
     * @param ex the authentication exception
     * @param request the current request
     * @return ResponseEntity with UNAUTHORIZED status
     */
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

    /**
     * Handles illegal arguments and duplicate entity exceptions.
     *
     * @param ex the exception
     * @param request the current request
     * @return ResponseEntity with CONFLICT status
     */
    @ExceptionHandler({IllegalArgumentException.class, AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse>handleIllegalArgumentException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    /**
     * Handles resource not found exceptions.
     *
     * @param ex the not found exception
     * @param request the current request
     * @return ResponseEntity with NOT_FOUND status
     */
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

    /**
     * Handles bad request exceptions.
     *
     * @param ex the exception
     * @param request the current request
     * @return ResponseEntity with BAD_REQUEST status
     */
    @ExceptionHandler({InvalidFileException.class, MaxUploadSizeExceededException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }

    /**
     * Handles all unhandled exceptions.
     *
     * @param ex the exception
     * @param request the current request
     * @return ResponseEntity with INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleInternalException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Une erreur interne est survenue.",
                        ((ServletWebRequest) request).getRequest().getRequestURI()
                )
        );
    }
}
