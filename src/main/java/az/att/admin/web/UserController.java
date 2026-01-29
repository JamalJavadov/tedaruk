package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.PortalUserCreateRequest;
import az.att.admin.service.impl.users.dto.PortalUserUpdateRequest;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import az.att.admin.service.impl.users.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserInfoResponse getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getUserInfo(userPrincipal);
    }

    @PutMapping("/me")
    public PortalUser updateUserContact(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody UserContactUpdateRequest request) {
        return userService.updateUserContact(userPrincipal, request);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
    public ResponseEntity<PortalUser> create(@Valid @RequestBody PortalUserCreateRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
    public ResponseEntity<Page<PortalUser>> list(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.listUsers(userPrincipal, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
    public ResponseEntity<PortalUser> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
    public ResponseEntity<PortalUser> update(@PathVariable UUID id,
                                             @Valid @RequestBody PortalUserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VIEW_SETTINGS')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
