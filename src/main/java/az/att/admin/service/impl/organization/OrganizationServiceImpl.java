package az.att.admin.service.impl.organization;

import az.att.admin.service.OrganizationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("local")
public class OrganizationServiceImpl implements OrganizationService {

    @Override
    public List<Organization> findByUser(String userId) {
        return List.of(Organization.builder()
                .name("ABC Şirkəti MMC")
                .tin("1001101112")
                .build());
    }
}
