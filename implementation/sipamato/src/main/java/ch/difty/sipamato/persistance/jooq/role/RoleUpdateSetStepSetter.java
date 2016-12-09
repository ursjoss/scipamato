package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;

import org.jooq.UpdateSetFirstStep;
import org.jooq.UpdateSetMoreStep;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.UpdateSetStepSetter;

/**
 * The update step setter used for updating {@link Role}s.<p/>
 *
 * <b>Note:</b> the {@link User}s are not updated here.
 *
 * @author u.joss
 */
@Component
public class RoleUpdateSetStepSetter implements UpdateSetStepSetter<RoleRecord, Role> {

    /** {@inheritDoc} */
    @Override
    public UpdateSetMoreStep<RoleRecord> setFieldsFor(UpdateSetFirstStep<RoleRecord> step, Role e) {
        AssertAs.notNull(step, "step");
        AssertAs.notNull(e, "entity");
        // @formatter:off
        return step
            .set(ROLE.NAME, e.getName())
            .set(ROLE.COMMENT, e.getComment());
        // @formatter:on
    }

}
