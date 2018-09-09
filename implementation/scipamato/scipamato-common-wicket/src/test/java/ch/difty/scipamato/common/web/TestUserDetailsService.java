package ch.difty.scipamato.common.web;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * This service has precedence over the productive implementations of
 * {@link UserDetailsService}. It will not be instantiated on production as it
 * is in the test package, so the productive implementation will be the only one.
 * <p>
 * We're using two hard-coded users, ignoring the database entirely.
 *
 * @author u.joss
 */
public class TestUserDetailsService implements UserDetailsService {

    private static final String ADMIN    = "testadmin";
    private static final String USER     = "testuser";
    private static final String VIEWER   = "testviewer";
    // BCrypt encrypted password 'secretpw' as defined in {@link WicketTest}
    private static final String PASSWORD = "$2a$08$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je";

    private final Map<String, User> users = new HashMap<>();

    public TestUserDetailsService() {
        users.put(ADMIN, new User(ADMIN, PASSWORD, Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER"))));
        users.put(USER, new User(USER, PASSWORD, Collections.singletonList(new Role("ROLE_USER"))));
        users.put(VIEWER, new User(VIEWER, PASSWORD, Collections.singletonList(new Role("ROLE_VIEWER"))));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = users.get(username);
        if (user == null)
            throw new UsernameNotFoundException("No user found with name " + username);
        else
            return user;
    }

    @Getter
    @AllArgsConstructor
    public static class User implements UserDetails {
        private static final long serialVersionUID = 1L;

        private final boolean accountNonExpired     = true;
        private final boolean accountNonLocked      = true;
        private final boolean credentialsNonExpired = true;
        private final boolean enabled               = true;

        private String     username;
        private String     password;
        private List<Role> roles;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            final String roleString = StringUtils.collectionToCommaDelimitedString(roles);
            return AuthorityUtils.commaSeparatedStringToAuthorityList(roleString);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Role implements GrantedAuthority {
        private static final long serialVersionUID = 1L;

        private String authority;
    }
}
