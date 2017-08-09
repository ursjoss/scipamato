package ch.difty.scipamato.persistance.jooq.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.SCIPAMATO_USER;

import java.util.List;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.lib.DateTimeService;
import ch.difty.scipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.scipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.scipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.scipamato.persistance.jooq.JooqSortMapper;
import ch.difty.scipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The repository to manage {@link User}s - including the nested list of {@link Roles}s.
 *
 * @author u.joss
 */
@Repository
@CacheDefaults(cacheName = "userByName")
public class JooqUserRepo extends JooqEntityRepo<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.db.tables.ScipamatoUser, UserRecordMapper, UserFilter> implements UserRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqUserRepo.class);

    private final UserRoleRepository userRoleRepo;

    @Autowired
    public JooqUserRepo(final DSLContext dsl, final UserRecordMapper mapper, final JooqSortMapper<ScipamatoUserRecord, User, ch.difty.scipamato.db.tables.ScipamatoUser> sortMapper,
            final GenericFilterConditionMapper<UserFilter> filterConditionMapper, final DateTimeService dateTimeService, final InsertSetStepSetter<ScipamatoUserRecord, User> insertSetStepSetter,
            final UpdateSetStepSetter<ScipamatoUserRecord, User> updateSetStepSetter, final ApplicationProperties applicationProperties, final UserRoleRepository userRoleRepo) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter, applicationProperties);
        this.userRoleRepo = AssertAs.notNull(userRoleRepo, "userRoleRepo");
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
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
    @CacheResult
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
    @CacheRemove(cacheKeyGenerator = UserNameOfUserCacheKeyGenerator.class)
    public User add(final User user) {
        return super.add(user);
    }

    @Override
    @CacheRemove(cacheKeyGenerator = UserNameOfUserCacheKeyGenerator.class)
    public User add(final User user, final String languageCode) {
        return super.add(user, languageCode);
    }

    @Override
    @CacheRemoveAll
    public User delete(final Integer id, final int version) {
        userRoleRepo.removeAllFromUserRoleCache();
        return super.delete(id, version);
    }

    @Override
    @CacheRemove(cacheKeyGenerator = UserNameOfUserCacheKeyGenerator.class)
    public User update(final User user) {
        if (user != null && user.getId() != null)
            userRoleRepo.removeFromUserRoleCache(user.getId());
        return super.update(user);
    }

    @Override
    @CacheRemove(cacheKeyGenerator = UserNameOfUserCacheKeyGenerator.class)
    public User update(final User user, final String languageCode) {
        if (user != null && user.getId() != null && languageCode != null)
            userRoleRepo.removeFromUserRoleCache(user.getId());
        return super.update(user, languageCode);
    }

}
