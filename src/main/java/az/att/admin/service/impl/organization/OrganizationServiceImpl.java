package az.att.admin.service.impl.organization;

import az.att.admin.repository.UserDetailRepository;
import az.att.admin.repository.OrganizationRepository;
import az.att.admin.service.OrganizationService;
import az.att.admin.service.impl.organization.dto.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("stage")
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final UserDetailRepository userCertificateRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> findByUser(String userId) {
        return userCertificateRepository.findAllByAsanUserId(UUID.fromString(userId))
                .stream()
                .map(cert -> organizationRepository.findOrganizationByTin(cert.getTin())
                        .map(orgEntity -> Organization.builder()
                                .tin(orgEntity.getTin())
                                .name(orgEntity.getName())
                                .hasStamp(cert.getHasStamp())
                                .isRegistered(true)
                                .build())
                        .orElseGet(() -> Organization.builder()
                                .tin(cert.getTin())
                                .name(cert.getStructureName())
                                .hasStamp(cert.getHasStamp())
                                .isRegistered(false)
                                .build()))
                .toList();
    }
}
