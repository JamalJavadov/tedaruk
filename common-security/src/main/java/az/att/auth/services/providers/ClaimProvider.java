package az.att.auth.services.providers;

import az.att.auth.services.Claim;
import org.springframework.security.core.Authentication;

public interface ClaimProvider {

    Claim provide(Authentication authentication);
}
