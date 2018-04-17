package ch.difty.scipamato.core.persistence.user;

import org.springframework.stereotype.Component;

import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.db.tables.records.ScipamatoUserRecord;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.persistence.AuditFields;
import ch.difty.scipamato.core.persistence.EntityRecordMapper;

/**
 * Record mapper mapping {@link ScipamatoUserRecord} into entity {@link User}.
 *
 *
 * <b>Note:</b> The mapper leaves the nested list of {@link Role}s empty.
 *
 * @author u.joss
 */
@Component
public class UserRecordMapper extends EntityRecordMapper<ScipamatoUserRecord, User> {

    @Override
    protected User makeEntity() {
        return new User();
    }

    @Override
    protected AuditFields getAuditFieldsOf(ScipamatoUserRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(),
            r.getVersion());
    }

    @Override
    protected void mapFields(ScipamatoUserRecord from, User to) {
        to.setId(from.getId());
        to.setUserName(from.getUserName());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setEnabled(from.getEnabled());
        to.setPassword(from.getPassword());
    }

}
