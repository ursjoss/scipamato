package ch.difty.scipamato.common.persistence;

import org.jooq.Condition;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

/**
 * Mapper translating the provided filter into matching conditions.
 *
 * @author u.joss
 *
 * @param <F>
 *            Filter class extending {@link ScipamatoFilter}
 */
@FunctionalInterface
public interface GenericFilterConditionMapper<F extends ScipamatoFilter> {

    /**
     * Map the provided filter into a jOOQ {@link Condition}
     *
     * @param filter
     *            filter class extending {@link ScipamatoFilter}
     * @return the Condition
     */
    Condition map(F filter);

}
