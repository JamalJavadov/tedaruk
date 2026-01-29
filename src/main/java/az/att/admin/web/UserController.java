package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public UserPrincipal getUser(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }

    @PutMapping("/me")
    public PortalUser updateUserContact(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestBody UserContactUpdateRequest request) {
        return service.updateUserContact(userPrincipal, request);
    }

}
