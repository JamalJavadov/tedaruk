package az.att.admin.service.impl.organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Organization {

    private String tin;
    private String name;
}
