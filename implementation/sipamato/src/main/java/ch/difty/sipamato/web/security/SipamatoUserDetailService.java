package ch.difty.sipamato.web.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.service.UserService;

/**
 * The implementation of {@link UserDetailsService} loads the user from the {@link UserService} and
 * wraps it into a a sipamato specific implementation of {@link UserDetails}.
 *
 * @author u.joss
 */
@Service("sipamatoUserDetailService")
public class SipamatoUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public SipamatoUserDetailService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final Optional<User> userOption = userService.findByUserName(username);
        if (!userOption.isPresent()) {
            throw new UsernameNotFoundException("No user found with name " + username);
        } else {
            return new SipamatoUserDetails(userOption.get());
        }
    }

}
