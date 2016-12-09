package ch.difty.sipamato.persistance.jooq.role;

import static ch.difty.sipamato.db.tables.Role.ROLE;

import java.util.List;

import org.jooq.Condition;

import ch.difty.sipamato.persistance.jooq.AbstractFilterConditionMapper;
import ch.difty.sipamato.persistance.jooq.FilterConditionMapper;

/**
 * Mapper turning the provider {@link RoleFilter} into a jOOQ {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class RoleFilterConditionMapper extends AbstractFilterConditionMapper<RoleFilter> {

    @Override
    public void map(final RoleFilter filter, final List<Condition> conditions) {
        if (filter.getNameMask() != null) {
            final String likeExpression = "%" + filter.getNameMask() + "%";
            conditions.add(ROLE.NAME.likeIgnoreCase(likeExpression));
        }
        if (filter.getCommentMask() != null) {
            final String likeExpression = "%" + filter.getCommentMask() + "%";
            conditions.add(ROLE.COMMENT.likeIgnoreCase(likeExpression));
        }
    }

}
