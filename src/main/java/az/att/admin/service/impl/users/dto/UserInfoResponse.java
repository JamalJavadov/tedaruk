package az.att.admin.service.impl.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private String pin;
    private String firstName;
    private String lastName;
    private String userId;
    private String tin;
    private List<String> permissions;
    private String mainRole;
    private String position;
    private String digitalPhoneNumber;
    private String gmail;
}
