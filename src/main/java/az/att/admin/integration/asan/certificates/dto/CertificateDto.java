package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.integration.asan.login.dto.StructureDataDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
    private StructureDataDto structureData;
    @JsonProperty("serialNumber")
    private String certificateNumber;
    private String voen;
  
}