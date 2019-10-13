package ch.difty.scipamato.core.web.authentication;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UserService;

/**
 * The implementation of {@link UserDetailsService} loads the user from the
 * {@link UserService} and wraps it into a a scipamato specific implementation
 * of {@link UserDetails}.
 *
 * @author u.joss
 */
@Service("userDetailsService")
public class ScipamatoUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public ScipamatoUserDetailsService(@NotNull final UserService userService) {
        this.userService = userService;
    }

    @NotNull
    @Override
    public UserDetails loadUserByUsername(@NotNull final String username) {
        final Optional<User> userOption = userService.findByUserName(username);
        final User user = userOption.orElseThrow(
            () -> new UsernameNotFoundException("No user found with name " + username));
        return new ScipamatoUserDetails(user);
    }
}
