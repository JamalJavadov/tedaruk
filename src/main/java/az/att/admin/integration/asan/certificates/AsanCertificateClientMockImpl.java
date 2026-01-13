package az.att.admin.integration.asan.certificates;

import az.att.admin.dto.LoginDetailDto;
import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
import az.att.admin.integration.asan.certificates.dto.CertificateData;
import az.att.admin.integration.asan.certificates.dto.CertificateDto;
import az.att.admin.integration.asan.login.dto.StructureDataDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class AsanCertificateClientMockImpl implements AsanCertificatesClient {

    @Override
    public AsanCertificatesResponseDto getCertificates(String accessToken) {
        StructureDataDto structureData = StructureDataDto.builder()
                .voen("123456789")
                .structureName("Test Company LLC")
                .position("Manager")
                .hasStamp(true)
                .legal(true)
                .build();

        CertificateDto certificateDto = CertificateDto.builder()
                .structureData(structureData)
                .build();

        LoginDetailDto loginDetail = LoginDetailDto.builder()
                .phoneNumber("+1234567890")
                .build();

        return AsanCertificatesResponseDto.builder()
                .loginDetail(loginDetail)
                .certificates(List.of(certificateDto))
                .build();
    }
}
