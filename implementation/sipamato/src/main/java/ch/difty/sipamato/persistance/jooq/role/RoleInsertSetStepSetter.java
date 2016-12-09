package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;

import org.jooq.InsertSetMoreStep;
import org.jooq.InsertSetStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.InsertSetStepSetter;

/**
 * The insert step setter used for inserting new {@link Role}s.<p/>
 *
 * <b>Note:</b> the {@link User}s are not inserted here.
 *
 * @author u.joss
 */
@Component
public class RoleInsertSetStepSetter implements InsertSetStepSetter<RoleRecord, Role> {

    /** {@inheritDoc} */
    @Override
    public InsertSetMoreStep<RoleRecord> setNonKeyFieldsFor(InsertSetStep<RoleRecord> step, Role e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");

        // @formatter:off
        return step
            .set(ROLE.NAME, e.getName())
            .set(ROLE.COMMENT, e.getComment());
        // @formatter:on
    }

    /** {@inheritDoc} */
    @Override
    public void considerSettingKeyOf(InsertSetMoreStep<RoleRecord> step, Role entity) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(entity, "entity");
        Integer id = entity.getId();
        if (id != null)
            step.set(ROLE.ID, id.intValue());
    }

}
