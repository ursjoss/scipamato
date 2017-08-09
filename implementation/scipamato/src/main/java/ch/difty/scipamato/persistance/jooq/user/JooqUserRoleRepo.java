package ch.difty.scipamato.persistance.jooq.user;

import static ch.difty.scipamato.db.tables.UserRole.USER_ROLE;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.db.tables.records.UserRoleRecord;
import ch.difty.scipamato.entity.User;

/**
 * The repository to manage {@link Role}s for {@link User}s.
 *
 * @author u.joss
 */
@Repository
@CacheConfig(cacheNames = "userRolesByUserId")
public class JooqUserRoleRepo implements UserRoleRepository {
    private static final long serialVersionUID = 1L;

    private final DSLContext dsl;

    @Autowired
    public JooqUserRoleRepo(final DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    @Cacheable(key = "#userId")
    public List<Role> findRolesForUser(final Integer userId) {
        return dsl.select(USER_ROLE.ROLE_ID).from(USER_ROLE).where(USER_ROLE.USER_ID.eq(userId)).fetch().map(rec -> Role.of((Integer) rec.getValue(0)));
    }

    @Override
    @CacheEvict(key = "#userId")
    public List<Role> addNewUserRolesOutOf(final Integer userId, final List<Role> roles) {
        InsertValuesStep2<UserRoleRecord, Integer, Integer> step = dsl.insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE_ID);
        for (final Role r : roles)
            step = step.values(userId, r.getId());
        step.onDuplicateKeyIgnore().execute();
        return findRolesForUser(userId);
    }

    @Override
    @CacheEvict(key = "#userId")
    public void deleteAllRolesExcept(final Integer userId, final List<Integer> roleIds) {
        dsl.deleteFrom(USER_ROLE).where(USER_ROLE.USER_ID.equal(userId).and(USER_ROLE.ROLE_ID.notIn(roleIds))).execute();
    }

}
