package ch.difty.scipamato.persistance.jooq.user;

import static ch.difty.scipamato.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static ch.difty.scipamato.db.tables.UserRole.USER_ROLE;

import java.util.List;
import java.util.stream.Collectors;

import javax.cache.annotation.CacheResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.scipamato.auth.Role;
import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.db.tables.records.UserRoleRecord;
import ch.difty.scipamato.entity.User;
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
public class JooqUserRepo extends JooqEntityRepo<ScipamatoUserRecord, User, Integer, ch.difty.scipamato.db.tables.ScipamatoUser, UserRecordMapper, UserFilter> implements UserRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqUserRepo.class);

    @Autowired
    public JooqUserRepo(DSLContext dsl, UserRecordMapper mapper, JooqSortMapper<ScipamatoUserRecord, User, ch.difty.scipamato.db.tables.ScipamatoUser> sortMapper,
            GenericFilterConditionMapper<UserFilter> filterConditionMapper, DateTimeService dateTimeService, InsertSetStepSetter<ScipamatoUserRecord, User> insertSetStepSetter,
            UpdateSetStepSetter<ScipamatoUserRecord, User> updateSetStepSetter, ApplicationProperties applicationProperties) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, insertSetStepSetter, updateSetStepSetter, applicationProperties);
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
    protected Integer getIdFrom(ScipamatoUserRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getIdFrom(User entity) {
        return entity.getId();
    }

    @Override
    protected void enrichAssociatedEntitiesOf(User entity, String languageCode) {
        if (entity != null) {
            final List<Role> roles = findRolesByName(entity);
            if (CollectionUtils.isNotEmpty(roles)) {
                entity.setRoles(roles);
            }
        }
    }

    @Override
    @CacheResult(cacheName = "userRolesByUserId")
    public List<Role> findRolesByName(User entity) {
        return getDsl().select(USER_ROLE.ROLE_ID).from(USER_ROLE).where(USER_ROLE.USER_ID.eq(entity.getId())).fetch().map(rec -> Role.of((Integer) rec.getValue(0)));
    }

    @Override
    protected void saveAssociatedEntitiesOf(User user, String languageCode) {
        storeNewRolesOf(user);
    }

    @Override
    protected void updateAssociatedEntities(User user, String languageCode) {
        storeNewRolesOf(user);
        deleteObsoleteRolesFrom(user);
    }

    private void storeNewRolesOf(User user) {
        InsertValuesStep2<UserRoleRecord, Integer, Integer> step = getDsl().insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE_ID);
        final Integer userId = user.getId();
        for (final Role r : user.getRoles()) {
            step = step.values(userId, r.getId());
        }
        step.onDuplicateKeyIgnore().execute();
    }

    private void deleteObsoleteRolesFrom(User user) {
        final List<Integer> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        getDsl().deleteFrom(USER_ROLE).where(USER_ROLE.USER_ID.equal(user.getId()).and(USER_ROLE.ROLE_ID.notIn(roleIds))).execute();
    }

    /** {@inheritDoc} */
    @Override
    @CacheResult(cacheName = "usersByName")
    public User findByUserName(String userName) {
        final List<User> users = getDsl().selectFrom(SCIPAMATO_USER).where(SCIPAMATO_USER.USER_NAME.eq(userName)).fetchInto(User.class);
        if (users.isEmpty()) {
            return null;
        } else {
            User user = users.get(0);
            enrichAssociatedEntitiesOf(user, null);
            return user;
        }
    }

}
