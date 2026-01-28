package az.att.admin.service.impl.organization.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {

    private String tin;
    private String name;
    private Boolean hasStamp;
    private Boolean isRegistered;
}
