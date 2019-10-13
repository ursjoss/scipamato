package ch.difty.scipamato.common.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

/**
 * Mapper translating the provided filter into matching conditions.
 *
 * @param <F>
 *     Filter class extending {@link ScipamatoFilter}
 * @author u.joss
 */
@FunctionalInterface
public interface GenericFilterConditionMapper<F extends ScipamatoFilter> {

    /**
     * Map the provided filter into a jOOQ {@link Condition}
     *
     * @param filter
     *     filter class extending {@link ScipamatoFilter}
     * @return the Condition
     */
    @NotNull
    Condition map(@Nullable F filter);

}
