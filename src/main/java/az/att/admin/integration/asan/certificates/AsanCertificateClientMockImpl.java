package az.att.admin.integration.asan.certificates;

import az.att.admin.integration.asan.certificates.dto.LoginDetailDto;
import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
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

                StructureDataDto structureData1 = StructureDataDto.builder()
                                .voen("1001101112")
                                .structureName("ABC Şirkəti MMC")
                                .position("Direktor")
                                .hasStamp(true)
                                .legal(true)
                                .certificateNumber("10000001")
                                .build();

                StructureDataDto structureData2 = StructureDataDto.builder()
                                .voen("1401102213")
                                .structureName("Tedaruk Test MMC")
                                .position("1-Direktor")
                                .hasStamp(true)
                                .legal(true)
                                .certificateNumber("100000002")
                                .build();

                StructureDataDto structureData3 = StructureDataDto.builder()
                                .voen("1001101114")
                                .structureName("QEYD Servis Şirkəti")
                                .position("Mütəxəssis")
                                .hasStamp(false)
                                .legal(true)
                                .certificateNumber("100000003")
                                .build();

                StructureDataDto structureData4 = StructureDataDto.builder()
                                .voen("1001101115")
                                .structureName("Test İstehsalat MMC")
                                .position("Baş mütəxəssis")
                                .hasStamp(true)
                                .legal(true)
                                .certificateNumber("100000004")
                                .build();

                List<CertificateDto> certificates = List.of(
                                CertificateDto.builder().structureData(structureData1).certificateNumber("10000001")
                                                .build(),
                                CertificateDto.builder().structureData(structureData2).certificateNumber("100000002")
                                                .build(),
                                CertificateDto.builder().structureData(structureData3).certificateNumber("100000003")
                                                .build(),
                                CertificateDto.builder().structureData(structureData4).certificateNumber("100000004")
                                                .build());

                LoginDetailDto loginDetail = LoginDetailDto.builder()
                                .phoneNumber("+994551234567")
                                .build();

                return AsanCertificatesResponseDto.builder()
                                .loginDetail(loginDetail)
                                .certificates(certificates)
                                .build();
        }
}
