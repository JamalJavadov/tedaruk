package az.att.admin.integration.asan.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructureDataDto {
    private String voen;
    private String structureName;
    private String position;
    private Boolean hasStamp;
    private Boolean legal;
    private String certificateNumber;

}