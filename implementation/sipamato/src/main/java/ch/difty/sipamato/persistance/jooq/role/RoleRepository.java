package ch.difty.sipamato.persistance.jooq.role;

import ch.difty.sipamato.db.tables.records.RoleRecord;
import ch.difty.sipamato.entity.Role;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface RoleRepository extends EntityRepository<RoleRecord, Role, Integer, RoleRecordMapper, RoleFilter> {

}
