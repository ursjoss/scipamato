package ch.difty.scipamato.persistence.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.*;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.DateTimeService;
import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.entity.filter.UserFilter;
import ch.difty.scipamato.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.persistence.InsertSetStepSetter;
import ch.difty.scipamato.persistence.JooqEntityRepo;
import ch.difty.scipamato.persistence.JooqSortMapper;
import ch.difty.scipamato.persistence.UpdateSetStepSetter;
import lombok.extern.slf4j.Slf4j;

/**
 * The repository to manage {@link User}s - including the nested list of Roles.
 *
 * @author u.joss
 */
@Repository
@Slf4j
@CacheConfig(cacheNames = "userByName")
public class JooqUserRepo extends JooqEntityRepo<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.db.tables.ScipamatoUser, UserRecordMapper, UserFilter> implements UserRepository {

    private static final long serialVersionUID = 1L;

    private final UserRoleRepository userRoleRepo;

    @Autowired
    public JooqUserRepo(@Qualifier("dslContext") final DSLContext dsl, final UserRecordMapper mapper, final JooqSortMapper<ScipamatoUserRecord, User, ch.difty.scipamato.db.tables.ScipamatoUser> sortMapper,
            final GenericFilterConditionMapper<UserFilter> filterConditionMapper, final DateTimeService dateTimeService, final InsertSetStepSetter<ScipamatoUserRecord, User> insertSetStepSetter,
            final UpdateSetStepSetter<ScipamatoUserRecord, User> updateSetStepSetter, final ApplicationProperties applicationProperties, final UserRoleRepository userRoleRepo) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter, applicationProperties);
        this.userRoleRepo = AssertAs.notNull(userRoleRepo, "userRoleRepo");
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Override
    protected Class<? extends User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<? extends ScipamatoUserRecord> getRecordClass() {
        return ScipamatoUserRecord.class;
    }

    @Override
    protected ch.difty.scipamato.db.tables.ScipamatoUser getTable() {
        return SCIPAMATO_USER;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getTableId() {
        return SCIPAMATO_USER.ID;
    }

    @Override
    protected TableField<ScipamatoUserRecord, Integer> getRecordVersion() {
        return SCIPAMATO_USER.VERSION;
    }

    @Override
    protected Integer getIdFrom(final ScipamatoUserRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getIdFrom(final User entity) {
        return entity.getId();
    }

    @Override
    protected void enrichAssociatedEntitiesOf(final User entity, final String languageCode) {
        if (entity != null) {
            final List<Role> roles = userRoleRepo.findRolesForUser(entity.getId());
            if (CollectionUtils.isNotEmpty(roles)) {
                entity.setRoles(roles);
            }
        }
    }

    @Override
    protected void saveAssociatedEntitiesOf(final User user, final String languageCode) {
        storeNewRolesOf(user);
    }

    @Override
    protected void updateAssociatedEntities(final User user, final String languageCode) {
        storeNewRolesOf(user);
        deleteObsoleteRolesFrom(user);
    }

    private void storeNewRolesOf(final User user) {
        userRoleRepo.addNewUserRolesOutOf(user.getId(), user.getRoles());
    }

    private void deleteObsoleteRolesFrom(final User user) {
        final Integer userId = user.getId();
        final List<Integer> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        userRoleRepo.deleteAllRolesExcept(userId, roleIds);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable
    public User findByUserName(final String userName) {
        final List<User> users = getDsl().selectFrom(SCIPAMATO_USER).where(SCIPAMATO_USER.USER_NAME.eq(userName)).fetchInto(User.class);
        if (users.isEmpty()) {
            return null;
        } else {
            final User user = users.get(0);
            enrichAssociatedEntitiesOf(user, null);
            return user;
        }
    }

    @Override
    @CachePut(cacheNames = "userByName", key = "#user.userName")
    public User add(final User user) {
        return super.add(user);
    }

    @Override
    @CachePut(cacheNames = "userByName", key = "#user.userName")
    public User add(final User user, final String languageCode) {
        return super.add(user, languageCode);
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "userByName", allEntries = true), @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User delete(final Integer id, final int version) {
        return super.delete(id, version);
    }

    @Override
    @Caching(put = { @CachePut(cacheNames = "userByName", key = "#user.userName") }, evict = { @CacheEvict(cacheNames = "userRolesByUserId", key = "#user.id", beforeInvocation = true) })
    public User update(final User user) {
        return super.update(user);
    }

    @Override
    @Caching(put = { @CachePut(cacheNames = "userByName", key = "#user.userName") }, evict = { @CacheEvict(cacheNames = "userRolesByUserId", key = "#user.id", beforeInvocation = true) })
    public User update(final User user, final String languageCode) {
        return super.update(user, languageCode);
    }

}
