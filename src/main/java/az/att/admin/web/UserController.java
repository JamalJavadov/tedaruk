package az.att.admin.web;

import az.att.admin.dto.UserFullResponseDto;
import az.att.admin.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserQueryService userQueryService;

    @PostMapping("/me")
    public ResponseEntity<UserFullResponseDto> getUser(@RequestHeader("att_token") String attToken) {
        return ResponseEntity.ok(userQueryService.getUserData(attToken));
    }
}
