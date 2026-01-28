package az.att.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.Locale;
import az.att.dto.ErrorResponseDto;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Primary
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private final AbstractMessageSource messageSource;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDto> handle(ApplicationException ex,
                                                   WebRequest request) {
        log.trace("Application exception occurred", ex);
        return ofType(request, ex.getErrorResponse().getHttpStatus(), ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handle(HttpMessageNotReadableException ex,
                                                  WebRequest request) {
        log.trace("Required request body is missing", ex);
        return ofType(request, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handle(AccessDeniedException ex,
                                                  WebRequest request) {
        log.trace("Access to the given resource is denied", ex);
        return ofType(request, HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponseDto> handle(ConstraintViolationException ex,
                                                        WebRequest request) {
        log.trace("Resource not found {}", ex.getMessage());
        List<ConstraintsViolationError> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ConstraintsViolationError(violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return ofType(request, HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), validationErrors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public final ResponseEntity<ErrorResponseDto> handle(MaxUploadSizeExceededException ex,
                                                        WebRequest request) {
        log.trace("Resource not found ", ex);
        return ofType(request, HttpStatus.PAYLOAD_TOO_LARGE, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorResponseDto> handle(MethodArgumentTypeMismatchException ex,
                                                        WebRequest request) {
        log.trace("Method arguments are not valid", ex);
        return ofType(request, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public final ResponseEntity<ErrorResponseDto> handle(MismatchedInputException ex,
                                                        WebRequest request) {
        log.trace("Mismatched inout ", ex);
        return ofType(request, HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(BindException.class)
    public final ResponseEntity<ErrorResponseDto> handle(
        BindException ex,
        WebRequest request) {
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ofType(request, HttpStatus.BAD_REQUEST, getLocalizedMessage(ex), validationErrors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponseDto> handle(
        MethodArgumentNotValidException ex,
        WebRequest request) {
        log.error("Method argument not valid", ex);
        List<ConstraintsViolationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ofType(request, HttpStatus.BAD_REQUEST, getLocalizedMessage(ex), validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handle(Exception ex,
                                                  WebRequest request) {
        log.error("Server failure", ex);
        return ofType(request, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handle(BadCredentialsException ex,
                                                  WebRequest request) {
        log.error("Bad credentials");
        return ofType(request, HttpStatus.UNAUTHORIZED, ex);
    }

    protected ResponseEntity<ErrorResponseDto> ofType(WebRequest request,
                                                     HttpStatus status, ApplicationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        return ofType(request, status, ex.getLocalizedMessage(locale, messageSource));
    }

    protected ResponseEntity<ErrorResponseDto> ofType(WebRequest request, HttpStatus status, Exception ex) {
        return ofType(request, status, getLocalizedMessage(ex));
    }

    private ResponseEntity<ErrorResponseDto> ofType(WebRequest request, HttpStatus status, String message) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(status.value())
                .error(getLocalizedReasonPhrase(status))
                .message(message)
                .path(path)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    private String getLocalizedMessage(Exception ex) {
        Locale locale = LocaleContextHolder.getLocale();
        var key = ex.getClass().getName() + ".message";
        try {
            return messageSource.getMessage(key, new Object[]{}, locale);
        } catch (NoSuchMessageException exception) {
            log.warn("Please consider adding localized message for key {} and locale {}", key, locale);
        }
        return ex.getMessage();
    }

    private String getLocalizedReasonPhrase(HttpStatus status) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(status.value() + ".message", new Object[]{}, locale);
        } catch (NoSuchMessageException exception) {
            log.warn("Please consider adding localized message for key {} and locale {}", status.value(), locale);
        }
        return status.getReasonPhrase();
    }

    private ResponseEntity<ErrorResponseDto> ofType(WebRequest request, HttpStatus status, String message, List<ConstraintsViolationError> validationErrors) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .status(status.value())
                .error(getLocalizedReasonPhrase(status))
                .message(message)
                .path(path)
                .validationErrors(validationErrors)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }
}
