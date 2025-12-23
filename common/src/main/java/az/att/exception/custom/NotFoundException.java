package az.att.exception.custom;

import az.att.exception.ApplicationException;
import az.att.exception.ErrorResponse;

import java.util.Map;

public class NotFoundException extends ApplicationException {
    public NotFoundException(ErrorResponse errorResponse, Map<String, Object> messageArguments) {
        super(errorResponse, messageArguments);
    }

    public NotFoundException(ErrorResponse errorResponse, Map<String, Object> messageArguments, Throwable cause) {
        super(errorResponse, messageArguments, cause);
    }

    public NotFoundException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public NotFoundException(ErrorResponse errorResponse, Throwable cause) {
        super(errorResponse, cause);
    }
}
