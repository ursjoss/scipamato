package ch.difty.scipamato.persistance.jooq.user;

import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.entity.filter.UserFilter;
import ch.difty.scipamato.persistance.jooq.EntityRepository;

public interface UserRepository extends EntityRepository<User, Integer, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName - must not be null
     * @return user or null
     */
    User findByUserName(String userName);

}
