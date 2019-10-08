package ch.difty.scipamato.core.web.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails;

/**
 * This service has precedence over the productive implementations of
 * {@link UserDetailsService}. It will not be instantiated on production as it
 * is in the test package, so the productive implementation will be the only one.
 * <p>
 * We're using two hard-coded users, ignoring the database entirely.
 *
 * @author u.joss
 */
@SuppressWarnings("SpellCheckingInspection")
@Service
@Primary
public class TestUserDetailsService implements UserDetailsService {

    public static final String USER_ADMIN  = "testadmin";
    public static final String USER_USER   = "testuser";
    public static final String USER_VIEWER = "testviewer";

    // BCrypt encrypted password 'secretpw' as defined in {@link WicketTest}
    private static final String PASSWORD = "$2a$08$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je";

    private final Map<String, User> users = new HashMap<>();

    public TestUserDetailsService() {
        users.put(USER_ADMIN, new User(1, USER_ADMIN, "a", "a", "a", PASSWORD, true, Set.of(Role.ADMIN, Role.USER)));
        users.put(USER_USER, new User(2, USER_USER, "t", "u", "tu", PASSWORD, true, Set.of(Role.USER)));
        users.put(USER_VIEWER, new User(3, USER_VIEWER, "v", "v", "vv", PASSWORD, true, Set.of(Role.VIEWER)));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with name " + username);
        } else {
            return new ScipamatoUserDetails(user);
        }
    }

}
