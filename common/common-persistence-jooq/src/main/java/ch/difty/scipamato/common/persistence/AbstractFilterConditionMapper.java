package ch.difty.scipamato.common.persistence;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

/**
 * Abstract class providing the basic algorithm as template method to map the
 * filter into the conditions.
 *
 * @param <F>
 *     Type of the filter class, extending {@link ScipamatoFilter}
 * @author u.joss
 */
public abstract class AbstractFilterConditionMapper<F extends ScipamatoFilter>
    implements GenericFilterConditionMapper<F> {

    @NotNull
    @Override
    public final Condition map(@Nullable final F filter) {
        final List<Condition> conditions = new ArrayList<>();
        if (filter != null)
            map(filter, conditions);
        return DSL.and(conditions);
    }

    /**
     * Maps the actual filter attributes into the provided list of conditions.
     *
     * @param filter
     *     provides the filter attributes to be mapped
     * @param conditions
     *     receives of the {@link Condition}s derived from the filter attributes
     */
    protected abstract void map(@NotNull F filter, @NotNull List<Condition> conditions);

}
