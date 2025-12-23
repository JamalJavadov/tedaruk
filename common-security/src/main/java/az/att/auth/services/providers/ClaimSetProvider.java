package az.att.auth.services.providers;

import az.att.auth.services.ClaimSet;
import org.springframework.security.core.Authentication;

public interface ClaimSetProvider {

    ClaimSet provide(Authentication authentication);
}
