package ch.difty.sipamato.persistance.jooq.user;

import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface UserRepository extends EntityRepository<User, Integer, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName
     * @return user or null
     */
    User findByUserName(String userName);

}
