package ch.difty.scipamato.core.persistence.search;

import static ch.difty.scipamato.core.db.tables.SearchOrder.SEARCH_ORDER;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.persistence.AbstractFilterConditionMapper;
import ch.difty.scipamato.common.persistence.FilterConditionMapper;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;

/**
 * Mapper turning the provider {@link SearchOrderFilter} into a jOOQ
 * {@link Condition}.
 *
 * @author u.joss
 */
@FilterConditionMapper
public class SearchOrderFilterConditionMapper extends AbstractFilterConditionMapper<SearchOrderFilter> {

    @Override
    public void map(@NotNull final SearchOrderFilter filter, @NotNull final List<Condition> conditions) {
        if (filter.getOwnerIncludingGlobal() != null) {
            conditions.add(
                DSL.or(SEARCH_ORDER.OWNER.equal(filter.getOwnerIncludingGlobal()), SEARCH_ORDER.GLOBAL.equal(true)));
        } else {
            if (filter.getNameMask() != null) {
                conditions.add(SEARCH_ORDER.NAME
                    .lower()
                    .contains(filter
                        .getNameMask()
                        .toLowerCase()));
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
