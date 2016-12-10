package ch.difty.sipamato.web.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.entity.User;

@Service("testUserDetailService")
@Primary
public class TestUserDetailService implements UserDetailsService {

    private static final String ADMIN = "testadmin";
    private static final String USER = "testuser";
    private static final String PASSWORD = "$2a$08$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je";

    private final Map<String, User> users = new HashMap<>();

    public TestUserDetailService() {
        users.put(ADMIN, new User(1, ADMIN, "a", "a", "a", PASSWORD, true, Arrays.asList(Role.ADMIN, Role.USER)));
        users.put(USER, new User(2, USER, "t", "u", "tu", PASSWORD, true, Arrays.asList(Role.USER)));
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with name " + username);
        } else {
            return new SipamatoUserDetails(user);
        }
    }

}
