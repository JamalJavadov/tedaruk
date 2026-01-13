package az.att.admin.integration.asan.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StructureDto {
    private Boolean hasStamp;
    private String voen;
    private String structureName;
    private Boolean legal;
    private String position;
}
