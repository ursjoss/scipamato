package ch.difty.scipamato.core.persistence;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;

/**
 * The {@link UserService} interface - defining {@link User} specific service methods.
 *
 * @author u.joss
 */
public interface UserService extends EntityService<Integer, User, UserFilter> {

    /**
     * Find {@link User} by username and return it as optional.
     *
     * @param userName
     *     the name of the user to be found
     * @return user optional of user
     */
    @NotNull
    Optional<User> findByUserName(@Nullable String userName);
}
