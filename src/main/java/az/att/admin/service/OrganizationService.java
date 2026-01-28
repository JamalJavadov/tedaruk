package az.att.admin.service;

import az.att.admin.service.impl.organization.dto.Organization;

import java.util.List;

public interface OrganizationService {

    List<Organization> findByUser(String userId);

}
