package az.att.admin.web;

import az.att.admin.service.impl.permission.dto.PermissionResponseDto;
import az.att.admin.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
@PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
public class PermissionController {

    private final PermissionService service;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions(){
        return ResponseEntity.ok(service.getModules());
    }



}
