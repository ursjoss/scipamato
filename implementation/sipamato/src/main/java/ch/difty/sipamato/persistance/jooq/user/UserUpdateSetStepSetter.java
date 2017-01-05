package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.DateUtils;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link User}s.<p/>
 *
 * <b>Note:</b> the {@link Role}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class UserUpdateSetStepSetter implements UpdateSetStepSetter<UserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<UserRecord> setFieldsFor(UpdateSetFirstStep<UserRecord> step, User e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        // @formatter:off
        return step
            .set(USER.USER_NAME, e.getUserName())
            .set(USER.FIRST_NAME, e.getFirstName())
            .set(USER.LAST_NAME, e.getLastName())
            .set(USER.EMAIL, e.getEmail())
            .set(USER.PASSWORD, e.getPassword())
            .set(USER.ENABLED, e.isEnabled())

            .set(USER.CREATED, DateUtils.tsOf(e.getCreated()))
            .set(USER.CREATED_BY, e.getCreatedBy())
            .set(USER.LAST_MODIFIED, DateUtils.tsOf(e.getLastModified()))
            .set(USER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(USER.VERSION, e.getVersion() + 1);
        // @formatter:on
    }

}
