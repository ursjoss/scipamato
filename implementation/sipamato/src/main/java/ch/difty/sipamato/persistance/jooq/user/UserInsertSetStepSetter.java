package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.SipamatoUser.SIPAMATO_USER;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.SipamatoUserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link User}s.<p>
 *
 * <b>Note:</b> the {@link Role}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class UserInsertSetStepSetter implements InsertSetStepSetter<SipamatoUserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public InsertSetMoreStep<SipamatoUserRecord> setNonKeyFieldsFor(InsertSetStep<SipamatoUserRecord> step, User e) {
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

            .set(SIPAMATO_USER.CREATED_BY, e.getCreatedBy())
            .set(SIPAMATO_USER.LAST_MODIFIED_BY, e.getLastModifiedBy());
        // @formatter:on
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<SipamatoUserRecord> step, User entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Integer id = entity.getId();
        if (id != null)
            step.set(SIPAMATO_USER.ID, id);
    }

    /** {@inheritDoc} */
    @Override
    public void resetIdToEntity(User entity, SipamatoUserRecord saved) {
        if (saved != null) {
            entity.setId(saved.getId());
        }
    }

}
