package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.impl.roles.dto.RoleCreateDto;
import az.att.admin.service.impl.roles.dto.RoleMiniResponseDto;
import az.att.admin.service.impl.roles.dto.RoleResponseDto;
import az.att.admin.service.impl.roles.dto.RoleUpdateDto;
import az.att.admin.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleMiniResponseDto> create(@Valid @RequestBody RoleCreateDto dto,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(service.create(dto, userPrincipal));
    }

    @GetMapping
    public ResponseEntity<Page<RoleMiniResponseDto>> listRoles(@PageableDefault(size = 20) Pageable pageable, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(service.listRoles(pageable, userPrincipal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> byId(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        service.softDelete(id, userPrincipal);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleMiniResponseDto> update(@Valid @RequestBody RoleUpdateDto dto, @PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(service.update(dto, id, userPrincipal));
    }

}
