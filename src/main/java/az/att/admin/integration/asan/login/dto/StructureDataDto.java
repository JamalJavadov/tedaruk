package az.att.admin.integration.asan.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StructureDataDto {
    private String voen;
    private String structureName;
    private String position;
    private Boolean hasStamp;
    private Boolean legal;
}