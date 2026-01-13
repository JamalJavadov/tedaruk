package az.att.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDetailDto {
    private String phoneNumber;
}
