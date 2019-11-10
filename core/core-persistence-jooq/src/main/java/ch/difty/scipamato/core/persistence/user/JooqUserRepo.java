package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.persistence.GenericFilterConditionMapper;
import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;
import ch.difty.scipamato.core.persistence.JooqEntityRepo;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;
import ch.difty.scipamato.core.persistence.UserRepository;

/**
 * The repository to manage {@link User}s - including the nested list of Roles.
 *
 * @author u.joss
 */
@Repository
@Slf4j
@Profile("!wickettest")
public class JooqUserRepo extends
    JooqEntityRepo<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.core.db.tables.ScipamatoUser, UserRecordMapper, UserFilter>
    implements UserRepository {

    private final UserRoleRepository userRoleRepo;

    public JooqUserRepo(@Qualifier("dslContext") @NotNull final DSLContext dsl, @NotNull final UserRecordMapper mapper,
        @NotNull final JooqSortMapper<ScipamatoUserRecord, User, ch.difty.scipamato.core.db.tables.ScipamatoUser> sortMapper,
        @NotNull final GenericFilterConditionMapper<UserFilter> filterConditionMapper,
        @NotNull final DateTimeService dateTimeService,
        @NotNull final InsertSetStepSetter<ScipamatoUserRecord, User> insertSetStepSetter,
        @NotNull final UpdateSetStepSetter<ScipamatoUserRecord, User> updateSetStepSetter,
        @NotNull final ApplicationProperties applicationProperties, @NotNull final UserRoleRepository userRoleRepo) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter,
            applicationProperties);
        this.userRoleRepo = userRoleRepo;
    }

    @NotNull
    @Override
    protected Logger getLogger() {
        return log;
    }

    @NotNull
    @Override
    protected ch.difty.scipamato.core.db.tables.ScipamatoUser getTable() {
        return SCIPAMATO_USER;
    }

    @NotNull
    @Override
    protected TableField<ScipamatoUserRecord, Integer> getTableId() {
        return SCIPAMATO_USER.ID;
    }

    @NotNull
    @Override
    protected TableField<ScipamatoUserRecord, Integer> getRecordVersion() {
        return SCIPAMATO_USER.VERSION;
    }

    @NotNull
    @Override
    protected Integer getIdFrom(@NotNull final ScipamatoUserRecord record) {
        return record.getId();
    }

    @NotNull
    @Override
    protected Integer getIdFrom(@NotNull final User entity) {
        return entity.getId();
    }

    @Override
    protected void enrichAssociatedEntitiesOf(@Nullable final User entity, @Nullable final String languageCode) {
        if (entity != null) {
            final Set<Role> roles = userRoleRepo.findRolesForUser(entity.getId());
            if (CollectionUtils.isNotEmpty(roles))
                entity.setRoles(roles);
        }
    }

    @Override
    protected void saveAssociatedEntitiesOf(@NotNull final User user, @NotNull final String languageCode) {
        storeNewRolesOf(user);
    }

    @Override
    protected void updateAssociatedEntities(@NotNull final User user, @NotNull final String languageCode) {
        storeNewRolesOf(user);
        deleteObsoleteRolesFrom(user);
    }

    private void storeNewRolesOf(final User user) {
        userRoleRepo.addNewUserRolesOutOf(user.getId(), user.getRoles());
    }

    private void deleteObsoleteRolesFrom(final User user) {
        final Integer userId = user.getId();
        final Set<Integer> roleIds = user
            .getRoles()
            .stream()
            .map(Role::getId)
            .collect(toSet());
        userRoleRepo.deleteAllRolesExcept(userId, roleIds);
    }

    @Nullable
    @Override
    @Cacheable(value = "userByName")
    public User findByUserName(@NotNull final String userName) {
        final List<User> users = getDsl()
            .selectFrom(SCIPAMATO_USER)
            .where(SCIPAMATO_USER.USER_NAME.eq(userName))
            .fetch(getMapper());
        if (users.isEmpty()) {
            return null;
        } else {
            final User user = users.get(0);
            enrichAssociatedEntitiesOf(user, null);
            return user;
        }
    }

    @Nullable
    @Override
    @Caching(put = { @CachePut(cacheNames = "userByName", key = "#user.userName") }, evict = {
        @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User add(@NotNull final User user) {
        return super.add(user);
    }

    @Nullable
    @Override
    @Caching(put = { @CachePut(cacheNames = "userByName", key = "#user.userName") }, evict = {
        @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User add(@NotNull final User user, @NotNull final String languageCode) {
        return super.add(user, languageCode);
    }

    @NotNull
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "userByName", allEntries = true),
        @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User delete(@NotNull final Integer id, final int version) {
        return super.delete(id, version);
    }

    @Nullable
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "userByName", allEntries = true),
        @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User update(@NotNull final User user) {
        return super.update(user);
    }

    @Nullable
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "userByName", allEntries = true),
        @CacheEvict(cacheNames = "userRolesByUserId", allEntries = true) })
    public User update(@NotNull final User user, @NotNull final String languageCode) {
        return super.update(user, languageCode);
    }
}
