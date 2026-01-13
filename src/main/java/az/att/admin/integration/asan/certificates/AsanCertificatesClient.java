package az.att.admin.integration.asan.certificates;

import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;

public interface AsanCertificatesClient {

    AsanCertificatesResponseDto getCertificates(String accessToken);
}
