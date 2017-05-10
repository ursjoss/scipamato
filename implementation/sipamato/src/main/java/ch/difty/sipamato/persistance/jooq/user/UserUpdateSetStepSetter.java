package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.SipamatoUser.SIPAMATO_USER;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.SipamatoUserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.DateUtils;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link User}s.<p>
 *
 * <b>Note:</b> the {@link Role}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class UserUpdateSetStepSetter implements UpdateSetStepSetter<SipamatoUserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<SipamatoUserRecord> setFieldsFor(UpdateSetFirstStep<SipamatoUserRecord> step, User e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        // @formatter:off
        return step
            .set(SIPAMATO_USER.USER_NAME, e.getUserName())
            .set(SIPAMATO_USER.FIRST_NAME, e.getFirstName())
            .set(SIPAMATO_USER.LAST_NAME, e.getLastName())
            .set(SIPAMATO_USER.EMAIL, e.getEmail())
            .set(SIPAMATO_USER.PASSWORD, e.getPassword())
            .set(SIPAMATO_USER.ENABLED, e.isEnabled())

            .set(SIPAMATO_USER.CREATED, DateUtils.tsOf(e.getCreated()))
            .set(SIPAMATO_USER.CREATED_BY, e.getCreatedBy())
            .set(SIPAMATO_USER.LAST_MODIFIED, DateUtils.tsOf(e.getLastModified()))
            .set(SIPAMATO_USER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(SIPAMATO_USER.VERSION, e.getVersion() + 1);
        // @formatter:on
    }

}
