package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;
import static ch.difty.sipamato.db.tables.User.USER;
import static ch.difty.sipamato.db.tables.UserRole.USER_ROLE;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.TableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.db.tables.records.UserRoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.DateTimeService;
import ch.difty.sipamato.persistance.jooq.GenericFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;
import ch.difty.sipamato.persistance.jooq.JooqEntityRepo;
import ch.difty.sipamato.persistance.jooq.JooqSortMapper;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;
import ch.difty.sipamato.service.Localization;

/**
 * The repository to manage {@link Role}s - including the nested list of {@link User}s.
 *
 * @author u.joss
 */
@Repository
public class JooqRoleRepo extends JooqEntityRepo<RoleRecord, Role, Integer, ch.difty.sipamato.db.tables.Role, RoleRecordMapper, RoleFilter> implements RoleRepository {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JooqRoleRepo.class);

    @Autowired
    public JooqRoleRepo(DSLContext dsl, RoleRecordMapper mapper, JooqSortMapper<RoleRecord, Role, ch.difty.sipamato.db.tables.Role> sortMapper,
            GenericFilterConditionMapper<RoleFilter> filterConditionMapper, DateTimeService dateTimeService, Localization localization, InsertSetStepSetter<RoleRecord, Role> insertSetStepSetter,
            UpdateSetStepSetter<RoleRecord, Role> updateSetStepSetter, Configuration jooqConfig) {
        super(dsl, mapper, sortMapper, filterConditionMapper, dateTimeService, localization, insertSetStepSetter, updateSetStepSetter, jooqConfig);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<? extends Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected Class<? extends RoleRecord> getRecordClass() {
        return RoleRecord.class;
    }

    @Override
    protected ch.difty.sipamato.db.tables.Role getTable() {
        return ROLE;
    }

    @Override
    protected TableField<RoleRecord, Integer> getTableId() {
        return ROLE.ID;
    }

    @Override
    protected Integer getIdFrom(RoleRecord record) {
        return record.getId();
    }

    @Override
    protected Integer getIdFrom(Role entity) {
        return entity.getId();
    }

    @Override
    protected void enrichAssociatedEntitiesOf(Role entity) {
        if (entity != null) {
            final List<User> users = getDsl().select().from(USER_ROLE).join(USER).on(USER_ROLE.USER_ID.eq(USER.ID)).where(USER_ROLE.ROLE_ID.eq(entity.getId())).fetchInto(User.class);
            if (CollectionUtils.isNotEmpty(users)) {
                entity.setUsers(users);
            }
        }
    }

    @Override
    protected void saveAssociatedEntitiesOf(Role role) {
        storeNewUsersOf(role);
    }

    @Override
    protected void updateAssociatedEntities(Role role) {
        storeNewUsersOf(role);
        deleteObsoleteUsersFrom(role);
    }

    private void storeNewUsersOf(Role role) {
        InsertValuesStep2<UserRoleRecord, Integer, Integer> step = getDsl().insertInto(USER_ROLE, USER_ROLE.USER_ID, USER_ROLE.ROLE_ID);
        final Integer roleId = role.getId();
        for (final User u : role.getUsers()) {
            step = step.values(u.getId(), roleId);
        }
        step.onDuplicateKeyIgnore().execute();
    }

    private void deleteObsoleteUsersFrom(Role role) {
        final List<Integer> ids = role.getUsers().stream().map(User::getId).collect(Collectors.toList());
        getDsl().deleteFrom(USER_ROLE).where(USER_ROLE.ROLE_ID.equal(role.getId()).and(USER_ROLE.USER_ID.notIn(ids))).execute();
    }

}
