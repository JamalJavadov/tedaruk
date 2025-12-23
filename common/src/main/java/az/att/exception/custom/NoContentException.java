package az.att.exception.custom;

import az.att.exception.ApplicationException;
import az.att.exception.ErrorResponse;

import java.util.Map;

public class NoContentException  extends ApplicationException {
    public NoContentException(ErrorResponse errorResponse, Map<String, Object> messageArguments) {
        super(errorResponse, messageArguments);
    }

    public NoContentException(ErrorResponse errorResponse, Map<String, Object> messageArguments, Throwable cause) {
        super(errorResponse, messageArguments, cause);
    }

    public NoContentException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public NoContentException(ErrorResponse errorResponse, Throwable cause) {
        super(errorResponse, cause);
    }
}
