package az.att.auth.services.providers;

import org.springframework.security.core.userdetails.UserDetails;

public interface PrincipalProvider {

    UserDetails provide(UserDetails user);

    UserDetails provide(UserDetails user, String tenantId);
}
