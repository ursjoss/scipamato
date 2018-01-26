package ch.difty.scipamato.common.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This service has precedence over the productive implementations of
 * {@UserDetailsService}. It will not be instantiated on production as it is in
 * the test package, so the productive implementation will be the only one.
 *
 * We're using two hard-coded users, ignoring the database entirely.
 *
 * @author u.joss
 */
@Service("testUserDetailService")
@Primary
public class TestUserDetailService implements UserDetailsService {

    private static final String ADMIN = "testadmin";
    private static final String USER  = "testuser";
    // BCrypt encrypted password 'secretpw' as defined in {@link WicketTest}
    private static final String PASSWORD = "$2a$08$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je";

    private final Map<String, User> users = new HashMap<>();

    public TestUserDetailService() {
        users.put(ADMIN, new User(ADMIN, PASSWORD, Arrays.asList(new Role("ROLE_ADMIN"), new Role("ROLE_USER"))));
        users.put(USER, new User(USER, PASSWORD, Arrays.asList(new Role("ROLE_USER"))));
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
            List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList(roleString);
            return list;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Role implements GrantedAuthority {
        private static final long serialVersionUID = 1L;

        private String authority;
    }
}
