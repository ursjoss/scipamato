package ch.difty.sipamato.persistance.jooq.user;

import static ch.difty.sipamato.db.tables.User.USER;

import java.util.List;

import org.jooq.Condition;

import ch.difty.sipamato.persistance.jooq.AbstractFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapper;

/**
 * Mapper turning the provider {@link UserFilter} into a jOOQ {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class UserFilterConditionMapper extends AbstractFilterConditionMapper<UserFilter> {

    @Override
    public void map(final UserFilter filter, final List<Condition> conditions) {
        if (filter.getNameMask() != null) {
            final String likeExpression = "%" + filter.getNameMask() + "%";
            conditions.add(USER.USER_NAME.likeIgnoreCase(likeExpression).or(USER.FIRST_NAME.likeIgnoreCase(likeExpression)).or(USER.LAST_NAME.likeIgnoreCase(likeExpression)));
        }
        if (filter.getEmailMask() != null) {
            final String likeExpression = "%" + filter.getEmailMask() + "%";
            conditions.add(USER.EMAIL.likeIgnoreCase(likeExpression));
        }
        if (filter.getEnabled() != null) {
            final boolean expression = filter.getEnabled();
            conditions.add(USER.ENABLED.eq(expression));
        }
    }

}
