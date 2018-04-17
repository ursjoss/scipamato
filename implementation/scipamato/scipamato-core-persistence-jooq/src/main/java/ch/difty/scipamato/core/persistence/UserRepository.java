package ch.difty.scipamato.core.persistence;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;

public interface UserRepository extends EntityRepository<User, Integer, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName
     *     - must not be null
     * @return user or null
     */
    User findByUserName(String userName);

}
