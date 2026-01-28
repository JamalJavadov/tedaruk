package az.att.admin.service.impl;

import az.att.admin.entity.PortalUserEntity;
import az.att.admin.integration.asan.certificates.dto.CertificateData;
import az.att.admin.entity.AsanUserCertificatesEntity;
import az.att.admin.integration.asan.certificates.dto.AsanCertificatesResponseDto;
import az.att.admin.integration.asan.certificates.dto.CertificateDto;
import az.att.admin.integration.asan.login.dto.StructureDataDto;
import az.att.admin.mapper.CertificateMapper;
import az.att.admin.repository.UserDetailRepository;
import az.att.admin.service.impl.users.UserMapper;
import az.att.admin.service.impl.users.dto.PortalUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import az.att.admin.entity.Organization;
import az.att.admin.repository.OrganizationRepository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCertificateServiceImpl {

    private final UserDetailRepository userDetailRepository;
    private final UserMapper userMapper;
    private final CertificateMapper certificateMapper;
    private final OrganizationRepository organizationRepository;

    // @ToDo: this should be in asan probably
    public List<CertificateDto> filterValidCertificates(AsanCertificatesResponseDto response) {
        if (response.getCertificates() == null) {
            return List.of();
        }

        return response.getCertificates().stream()
                .filter(cert -> cert.getStructureData() != null)
                .filter(cert -> cert.getStructureData().getVoen() != null)
                .filter(cert -> !cert.getStructureData().getVoen().trim().isEmpty())
                .toList();
    }

    // @ToDo: fix thi section use organization
    public void saveUserCertificates(PortalUser user, List<CertificateDto> certificates) {
        for (CertificateDto cert : certificates) {
            StructureDataDto structureData = cert.getStructureData();
            CertificateData certificateData = buildCertificateData(structureData, cert.getCertificateNumber());
            saveOrUpdateCertificate(user, certificateData);
        }
    }

    private void saveOrUpdateCertificate(PortalUser user, CertificateData certificateData) {
        PortalUserEntity userEntity = userMapper.toEntity(user);
        AsanUserCertificatesEntity certificate = userDetailRepository
                .findByAsanUserAndTin(userEntity, certificateData.getVoen())
                .orElseGet(() -> {
                    AsanUserCertificatesEntity newCert = new AsanUserCertificatesEntity();
                    newCert.setAsanUser(userEntity);
                    newCert.setTin(certificateData.getVoen());
                    return newCert;
                });

        certificateMapper.updateEntityFromDto(certificateData, certificate);
        Optional<Organization> organizationOpt = organizationRepository
                .findOrganizationByTin(certificateData.getVoen());
        if (organizationOpt.isPresent()) {
            // If the Organizations already exsists
            certificate.setOrganization(organizationOpt.get());
        } else if (Boolean.TRUE.equals(certificateData.getHasStamp())
                && StringUtils.hasText(certificateData.getPosition())
                && certificateData.getPosition().startsWith("1")) {
            // There is no organization, but there is a seal...
            Organization newOrg = Organization.builder()
                    .tin(certificateData.getVoen())
                    .name(certificateData.getStructureName())
                    .build();
            Organization savedOrg = organizationRepository.save(newOrg);
            certificate.setOrganization(savedOrg);
        }

        userDetailRepository.save(certificate);
    }

    private CertificateData buildCertificateData(StructureDataDto structureData, String certificateNumber) {
        return CertificateData.builder()
                .certificateNumber(certificateNumber)
                .voen(structureData.getVoen())
                .structureName(structureData.getStructureName())
                .position(structureData.getPosition())
                .hasStamp(structureData.getHasStamp())
                .legal(structureData.getLegal())
                .build();
    }
}
