package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link User}s.<p/>
 *
 * <b>Note:</b> the {@link Role}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class UserInsertSetStepSetter implements InsertSetStepSetter<UserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public InsertSetMoreStep<UserRecord> setNonKeyFieldsFor(InsertSetStep<UserRecord> step, User e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

        // @formatter:off
        return step
            .set(USER.USER_NAME, e.getUserName())
            .set(USER.FIRST_NAME, e.getFirstName())
            .set(USER.LAST_NAME, e.getLastName())
            .set(USER.EMAIL, e.getEmail())
            .set(USER.PASSWORD, e.getPassword())
            .set(USER.ENABLED, e.isEnabled());
        // @formatter:on
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<UserRecord> step, User entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Integer id = entity.getId();
        if (id != null)
            step.set(USER.ID, id.intValue());
    }

}
