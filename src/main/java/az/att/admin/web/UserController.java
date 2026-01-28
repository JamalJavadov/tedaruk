package az.att.admin.web;

import az.att.admin.config.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public UserPrincipal getUser(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }

}
