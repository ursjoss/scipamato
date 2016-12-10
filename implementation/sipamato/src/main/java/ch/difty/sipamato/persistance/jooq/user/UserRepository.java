package ch.difty.sipamato.persistance.jooq.user;

import ch.difty.sipamato.db.tables.records.UserRecord;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.EntityRepository;

public interface UserRepository extends EntityRepository<UserRecord, User, Integer, UserRecordMapper, UserFilter> {

    /**
     * Find user by name
     *
     * @param userName
     * @return user or null
     */
    User findByUserName(String userName);

}
