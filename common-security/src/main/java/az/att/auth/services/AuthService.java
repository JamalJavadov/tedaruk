package az.att.auth.services;

import org.springframework.security.core.Authentication;
import java.util.Optional;

public interface AuthService {

    /**
     * Extract authentication object out of token.
     *
     * @param token : JWT or Bearer token
     * @return : extracted Authentication
     */
    Optional<Authentication> getAuthentication(String token);
}
