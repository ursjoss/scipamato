package ch.difty.scipamato.core.persistence.user;

import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.filter.UserFilter;
import ch.difty.scipamato.core.persistence.EntityRepository;

public interface UserRepository extends EntityRepository<User, Integer, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName - must not be null
     * @return user or null
     */
    User findByUserName(String userName);

}
