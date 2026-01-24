package az.att.admin.service;

import az.att.admin.service.impl.permission.dto.PermissionResponseDto;

import java.util.List;

public interface PermissionService {

    List<PermissionResponseDto> getModules();
}
