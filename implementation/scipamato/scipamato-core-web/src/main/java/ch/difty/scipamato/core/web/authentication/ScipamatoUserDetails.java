package ch.difty.scipamato.core.web.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import lombok.EqualsAndHashCode;

/**
 * Implementation of {@link UserDetails} working the the {@link User} entity.
 */
@EqualsAndHashCode(callSuper = true)
public class ScipamatoUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final List<Role> roles;

    public ScipamatoUserDetails(final User user) {
        super(user);
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final String roleString = StringUtils.collectionToCommaDelimitedString(roles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roleString);
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
