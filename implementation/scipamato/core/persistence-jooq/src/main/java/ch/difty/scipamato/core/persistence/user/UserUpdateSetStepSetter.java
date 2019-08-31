package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;

import java.sql.Timestamp;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.UtilsKt;
import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link User}s.
 *
 *
 * <b>Note:</b> the {@link Role}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class UserUpdateSetStepSetter implements UpdateSetStepSetter<ScipamatoUserRecord, User> {

    @Override
    public UpdateSetMoreStep<ScipamatoUserRecord> setFieldsFor(UpdateSetFirstStep<ScipamatoUserRecord> step, User e) {
        AssertAs.INSTANCE.notNull(step, "step");

        final Timestamp created = e.getLastModified() == null ? null : UtilsKt.toTimestamp(e.getCreated());
        final Timestamp lastMod = e.getLastModified() == null ? null : UtilsKt.toTimestamp(e.getLastModified());
        final UpdateSetMoreStep<ScipamatoUserRecord> step1 = step
            .set(SCIPAMATO_USER.USER_NAME, e.getUserName())
            .set(SCIPAMATO_USER.FIRST_NAME, e.getFirstName())
            .set(SCIPAMATO_USER.LAST_NAME, e.getLastName())
            .set(SCIPAMATO_USER.EMAIL, e.getEmail());
        final String password = e.getPassword();
        final UpdateSetMoreStep<ScipamatoUserRecord> step2 =
            password != null ? step1.set(SCIPAMATO_USER.PASSWORD, password) : step1;
        return step2
            .set(SCIPAMATO_USER.ENABLED, e.isEnabled())

            .set(SCIPAMATO_USER.CREATED, created)
            .set(SCIPAMATO_USER.CREATED_BY, e.getCreatedBy())
            .set(SCIPAMATO_USER.LAST_MODIFIED, lastMod)
            .set(SCIPAMATO_USER.LAST_MODIFIED_BY, e.getLastModifiedBy())
            .set(SCIPAMATO_USER.VERSION, e.getVersion() + 1);
    }

}
