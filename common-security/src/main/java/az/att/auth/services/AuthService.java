package az.att.auth.services;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface AuthService {

    /**
     * Extract authentication object out of request.
     *
     * @param httpServletRequest : the http servlet request
     * @return : extracted Authentication
     */
    Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest);
}
