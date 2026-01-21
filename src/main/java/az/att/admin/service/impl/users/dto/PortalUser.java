package az.att.admin.service.impl.users.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PortalUser {
    private UUID id;
    private String phoneNumber;
    private String pin;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String citizenship;
    private LocalDateTime lastActiveTime;
}
