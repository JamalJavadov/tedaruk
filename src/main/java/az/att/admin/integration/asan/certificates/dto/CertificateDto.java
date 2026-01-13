package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.integration.asan.login.dto.StructureDataDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificateDto {
    private StructureDataDto structureData;
}