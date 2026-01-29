package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import az.att.admin.service.UserService;
import az.att.admin.service.impl.users.dto.PortalUser;
import az.att.admin.service.impl.users.dto.UserContactUpdateRequest;
import az.att.admin.service.impl.users.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
