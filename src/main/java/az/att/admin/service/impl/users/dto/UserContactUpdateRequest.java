package az.att.admin.service.impl.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContactUpdateRequest {
    private String digitalPhoneNumber;
    private String gmail;
}
