package az.att.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private String error;
    private String message;
    private String path;
    private Integer status;
    private java.util.List<az.att.exception.ConstraintsViolationError> validationErrors;
}
