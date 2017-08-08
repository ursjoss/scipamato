package ch.difty.scipamato.persistance.jooq.user;

import java.util.List;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.persistance.jooq.EntityRepository;

public interface UserRepository extends EntityRepository<User, Integer, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName - must not be null
     * @return user or null
     */
    User findByUserName(String userName);

    List<Role> findRolesByName(User entity);

}
