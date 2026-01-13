package az.att.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVariablesDto {
    private String key;
    private String value;
}
