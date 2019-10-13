package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link User}s.
 *
 *
 * <b>Note:</b> the {@link Role}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class UserInsertSetStepSetter implements InsertSetStepSetter<ScipamatoUserRecord, User> {

    @NotNull
    @Override
    public InsertSetMoreStep<ScipamatoUserRecord> setNonKeyFieldsFor(@NotNull InsertSetStep<ScipamatoUserRecord> step,
        @NotNull User e) {
        return step
            .set(SCIPAMATO_USER.USER_NAME, e.getUserName())
            .set(SCIPAMATO_USER.FIRST_NAME, e.getFirstName())
            .set(SCIPAMATO_USER.LAST_NAME, e.getLastName())
            .set(SCIPAMATO_USER.EMAIL, e.getEmail())
            .set(SCIPAMATO_USER.PASSWORD, e.getPassword())
            .set(SCIPAMATO_USER.ENABLED, e.isEnabled())

            .set(SCIPAMATO_USER.CREATED_BY, e.getCreatedBy())
            .set(SCIPAMATO_USER.LAST_MODIFIED_BY, e.getLastModifiedBy());
    }

    @Override
    public void considerSettingKeyOf(@NotNull InsertSetMoreStep<ScipamatoUserRecord> step, @NotNull User entity) {
        Integer id = entity.getId();
        if (id != null)
            step.set(SCIPAMATO_USER.ID, id);
    }

    @Override
    public void resetIdToEntity(@NotNull User entity, @Nullable ScipamatoUserRecord saved) {
        if (saved != null)
            entity.setId(saved.getId());
    }
}
