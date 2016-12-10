package ch.difty.sipamato.persistance.jooq.user;

import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.lib.AssertAs;

/**
 * Record mapper mapping {@link UserRecord} into entity {@link User}.<p/>
 *
 * <b>Note:</b> The mapper leaves the nested list of {@link Role}s empty.
 *
 * @author u.joss
 */
@Component
public class UserRecordMapper implements RecordMapper<UserRecord, User> {

    /** {@inheritDoc} */
    @Override
    public User map(UserRecord from) {
        AssertAs.notNull(from, "from");

        User role = new User(from.getId(), from.getUserName(), from.getFirstName(), from.getLastName(), from.getEmail(), from.getPassword());
        role.setEnabled(from.getEnabled());
        return role;
    }

}
