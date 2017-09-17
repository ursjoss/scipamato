package ch.difty.scipamato.persistance.jooq.search;

import static ch.difty.scipamato.db.tables.SearchOrder.*;

import java.util.List;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.persistance.jooq.AbstractFilterConditionMapper;
import ch.difty.scipamato.persistance.jooq.FilterConditionMapper;

/**
 * Mapper turning the provider {@link SearchOrderFilter} into a jOOQ {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class SearchOrderFilterConditionMapper extends AbstractFilterConditionMapper<SearchOrderFilter> {

    @Override
    public void map(final SearchOrderFilter filter, final List<Condition> conditions) {
        if (filter.getOwnerIncludingGlobal() != null) {
            conditions.add(DSL.or(SEARCH_ORDER.OWNER.equal(filter.getOwnerIncludingGlobal()), SEARCH_ORDER.GLOBAL.equal(true)));
        } else {
            if (filter.getNameMask() != null) {
                conditions.add(SEARCH_ORDER.NAME.lower().contains(filter.getNameMask().toLowerCase()));
            }
            if (filter.getOwner() != null) {
                conditions.add(SEARCH_ORDER.OWNER.equal(filter.getOwner()));
            }

            if (filter.getGlobal() != null) {
                conditions.add(SEARCH_ORDER.GLOBAL.equal(filter.getGlobal()));
            }
        }

    }

}
