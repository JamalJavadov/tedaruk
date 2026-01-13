package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.dto.LoginDetailDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsanCertificatesResponseDto {
    private LoginDetailDto loginDetail;
    private List<CertificateDto> certificates;
}
