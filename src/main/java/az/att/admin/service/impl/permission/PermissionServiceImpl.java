package az.att.admin.service.impl.permission;

import az.att.admin.service.impl.permission.dto.PermissionResponseDto;
import az.att.admin.mapper.permission.PermissionMapper;
import az.att.admin.repository.PermissionRepository;
import az.att.admin.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;


    public List<PermissionResponseDto> getModules(){
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .toList();
    }

}
