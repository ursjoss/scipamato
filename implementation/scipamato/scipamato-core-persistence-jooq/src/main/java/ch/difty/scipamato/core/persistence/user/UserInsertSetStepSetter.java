package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link User}s.
 * <p>
 *
 * <b>Note:</b> the {@link Role}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class UserInsertSetStepSetter implements InsertSetStepSetter<ScipamatoUserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public InsertSetMoreStep<ScipamatoUserRecord> setNonKeyFieldsFor(InsertSetStep<ScipamatoUserRecord> step, User e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

        return step.set(SCIPAMATO_USER.USER_NAME, e.getUserName())
            .set(SCIPAMATO_USER.FIRST_NAME, e.getFirstName())
            .set(SCIPAMATO_USER.LAST_NAME, e.getLastName())
            .set(SCIPAMATO_USER.EMAIL, e.getEmail())
            .set(SCIPAMATO_USER.PASSWORD, e.getPassword())
            .set(SCIPAMATO_USER.ENABLED, e.isEnabled())

            .set(SCIPAMATO_USER.CREATED_BY, e.getCreatedBy())
            .set(SCIPAMATO_USER.LAST_MODIFIED_BY, e.getLastModifiedBy());
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<ScipamatoUserRecord> step, User entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Integer id = entity.getId();
        if (id != null)
            step.set(SCIPAMATO_USER.ID, id);
    }

    /** {@inheritDoc} */
    @Override
    public void resetIdToEntity(User entity, ScipamatoUserRecord saved) {
        if (saved != null) {
            entity.setId(saved.getId());
        }
    }

}
