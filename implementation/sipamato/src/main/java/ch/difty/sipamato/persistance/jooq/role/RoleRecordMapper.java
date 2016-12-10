package ch.difty.sipamato.persistance.jooq.role;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Record mapper mapping {@link RoleRecord} into entity {@link Role}.<p/>
 *
 * <b>Note:</b> The mapper leaves the nested list of {@link User}s empty.
 *
 * @author u.joss
 */
@Component
public class RoleRecordMapper implements RecordMapper<RoleRecord, Role> {

    /** {@inheritDoc} */
    @Override
    public Role map(RoleRecord from) {
        AssertAs.notNull(from, "from");

        Role role = new Role(from.getId(), from.getName());
        role.setComment(from.getComment());
        return role;
    }

}
