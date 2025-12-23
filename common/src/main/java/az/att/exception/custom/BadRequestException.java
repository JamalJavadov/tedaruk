package az.att.exception.custom;

import az.att.exception.ApplicationException;
import az.att.exception.ErrorResponse;

import java.util.Map;

public class BadRequestException extends ApplicationException {
    public BadRequestException(ErrorResponse errorResponse, Map<String, Object> messageArguments) {
        super(errorResponse, messageArguments);
    }

    public BadRequestException(ErrorResponse errorResponse, Map<String, Object> messageArguments, Throwable cause) {
        super(errorResponse, messageArguments, cause);
    }

    public BadRequestException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public BadRequestException(ErrorResponse errorResponse, Throwable cause) {
        super(errorResponse, cause);
    }
}
