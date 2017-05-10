package ch.difty.sipamato.persistance.jooq.user;

import org.springframework.stereotype.Component;

import ch.difty.sipamato.auth.Role;
import ch.difty.sipamato.db.tables.records.SipamatoUserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.AuditFields;
import ch.difty.sipamato.persistance.jooq.EntityRecordMapper;

/**
 * Record mapper mapping {@link SipamatoUserRecord} into entity {@link User}.<p>
 *
 * <b>Note:</b> The mapper leaves the nested list of {@link Role}s empty.
 *
 * @author u.joss
 */
@Component
public class UserRecordMapper extends EntityRecordMapper<SipamatoUserRecord, User> {

    @Override
    protected User makeEntity() {
        return new User();
    }

    @Override
    protected AuditFields getAuditFieldsOf(SipamatoUserRecord r) {
        return new AuditFields(r.getCreated(), r.getCreatedBy(), r.getLastModified(), r.getLastModifiedBy(), r.getVersion());
    }

    @Override
    protected void mapFields(SipamatoUserRecord from, User to) {
        to.setId(from.getId());
        to.setUserName(from.getUserName());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setEnabled(from.getEnabled());
        to.setPassword(from.getPassword());
    }

}
