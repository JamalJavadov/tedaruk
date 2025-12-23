package az.att.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrors implements ErrorResponse {

    jakarta_SIZE_MESSAGE("jakarta.validation.constraints.Size.message", HttpStatus.BAD_REQUEST,
            "dəyər minimum {min} və {max} simvol uzunluqda olmalıdır"),
    jakarta_NOT_NULL("jakarta.validation.constraints.NotNull.message", HttpStatus.BAD_REQUEST, "dəyər boş ola bilməz"),
    jakarta_NOT_BLANK("jakarta.validation.constraints.NotBlank.message", HttpStatus.BAD_REQUEST, "dəyər boş ola bilməz"),
    jakarta_METHOD_ARGUMENT_NOT_VALID("org.springframework.web.bind.MethodArgumentNotValidException.message",
            HttpStatus.BAD_REQUEST, "Data length exceeds limits."),
    IBAN_VALIDATION("az.ingress.user.management.validation.ValidIban.message", HttpStatus.BAD_REQUEST,
            "Iban formatı yalnışdır"),
    BAD_CREDENTIALS("org.springframework.security.authentication.BadCredentialsException.message",
            HttpStatus.BAD_REQUEST, "Login və ya parol yalnışdır"),
    ACCESS_DENIED("org.springframework.security.access.AccessDeniedException.message",
            HttpStatus.FORBIDDEN, "Bu əməliyyat üçün icazəniz yoxudr."),
    HTTP_400("MethodArgumentNotValidException.message", HttpStatus.BAD_REQUEST, "Bad request"),
    HTTP_500("500.message", HttpStatus.BAD_REQUEST, "System error"),
    HTTP_401("401.message", HttpStatus.BAD_REQUEST, "Authentication failed"),
    HTTP_404("404.message", HttpStatus.BAD_REQUEST, "Not found"),
    HTTP_403("403.message", HttpStatus.FORBIDDEN, "Authorization failed"),
    ENTITY_NOT_FOUND("Entity not found",HttpStatus.NOT_FOUND,"Not found");
    String key;
    HttpStatus httpStatus;
    String message;

    CommonErrors(String key, HttpStatus httpStatus, String message) {
        this.message = message;
        this.key = key;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
