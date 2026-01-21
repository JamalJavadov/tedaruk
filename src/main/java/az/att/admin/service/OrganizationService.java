package az.att.admin.service;

import az.att.admin.service.impl.organization.Organization;

import java.util.List;

public interface OrganizationService {

    List<Organization> findByUser(String userId);

}
