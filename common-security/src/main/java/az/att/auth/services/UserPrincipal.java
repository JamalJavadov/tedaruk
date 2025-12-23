package az.att.auth.services;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
@EqualsAndHashCode
public class UserPrincipal implements UserDetails {

    private UserDetails userDetails;
    private String tenantId;

    public UserPrincipal(UserDetails userDetails, String tenantId) {
        this.userDetails = userDetails;
        this.tenantId = tenantId;
    }

    public String getTenantId() {
        return tenantId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getPassword() {
        return userDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDetails.isCredentialsNonExpired();
    }

    public static UserPrincipal create(UserDetails userDetails, String tenantId) {
        return new UserPrincipal(userDetails, tenantId);
    }

    @Override
    public boolean isEnabled() {
        return userDetails.isEnabled();
    }
}
