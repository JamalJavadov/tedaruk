package az.att.admin.web;

import az.att.admin.integration.iamas.IamasClient;
import az.att.admin.integration.iamas.dto.IdCardSimpleResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/people/")
@RequiredArgsConstructor
@Tag(name = "IAMAS Integration", description = "Endpoints for fetching ID card information")
public class IamasController {

    private final IamasClient iamasClient;

    @GetMapping("/id-cards")
    @PreAuthorize("principal.hasStamp == true and hasAuthority('VIEW_SETTINGS')")
    @Operation(summary = "Get ID Card List by series and pin")
    public ResponseEntity<IdCardSimpleResponseDto> getIdCardList(
            @RequestParam String docNumber,
            @RequestParam String pin,
            Authentication authentication) {
        return iamasClient.getIdCardList(docNumber, pin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
