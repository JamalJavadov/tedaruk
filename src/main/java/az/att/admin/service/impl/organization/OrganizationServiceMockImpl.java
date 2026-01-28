package az.att.admin.service.impl.organization;

import az.att.admin.service.OrganizationService;
import az.att.admin.service.impl.organization.dto.Organization;
import az.att.admin.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Profile("local")
@RequiredArgsConstructor
public class OrganizationServiceMockImpl implements OrganizationService {

    private final UserDetailRepository userDetailRepository;

    @Override
    public List<Organization> findByUser(String userId) {
        return userDetailRepository.findAllByAsanUserId(UUID.fromString(userId))
                .stream()
                .map(cert -> Organization.builder()
                        .tin(cert.getTin())
                        .name(cert.getStructureName())
                        .hasStamp(cert.getHasStamp())
                        .isRegistered(false)
                        .build())
                .toList();
    }
}
