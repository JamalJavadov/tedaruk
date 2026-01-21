package az.att.admin.integration.asan.certificates.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsanCertificatesResponseDto {
    private LoginDetailDto loginDetail;
    private List<CertificateDto> certificates;
}
