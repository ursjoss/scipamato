package ch.difty.sipamato.persistance.jooq;

import org.jooq.Condition;

import ch.difty.sipamato.entity.SipamatoFilter;

/**
 * Mapper translating the provided filter into matching conditions.
 *
 * @author u.joss
 *
 * @param <F> Filter class extending {@link SipamatoFilter}
 */
@FunctionalInterface
public interface GenericFilterConditionMapper<F extends SipamatoFilter> {

    /**
     * Map the provided filter into a jOOQ {@link Condition}
     *
     * @param filter filter class extending {@link SipamatoFilter}
     * @return the Condition
     */
    Condition map(F filter);

}
