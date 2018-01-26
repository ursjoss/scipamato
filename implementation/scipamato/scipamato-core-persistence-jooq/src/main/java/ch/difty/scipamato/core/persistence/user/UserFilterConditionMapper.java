package ch.difty.scipamato.core.persistence.user;

import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;

import java.util.List;

import org.jooq.Condition;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.entity.search.UserFilter;

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
            conditions.add(SCIPAMATO_USER.USER_NAME.likeIgnoreCase(likeExpression)
                .or(SCIPAMATO_USER.FIRST_NAME.likeIgnoreCase(likeExpression))
                .or(SCIPAMATO_USER.LAST_NAME.likeIgnoreCase(likeExpression)));
        }
        if (filter.getEmailMask() != null) {
            final String likeExpression = "%" + filter.getEmailMask() + "%";
            conditions.add(SCIPAMATO_USER.EMAIL.likeIgnoreCase(likeExpression));
        }
        if (filter.getEnabled() != null) {
            final boolean expression = filter.getEnabled();
            conditions.add(SCIPAMATO_USER.ENABLED.eq(expression));
        }
    }

}
