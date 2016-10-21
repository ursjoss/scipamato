package ch.difty.sipamato.persistance.jooq;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.SipamatoFilter;

/**
 * Abstract class providing the basic algorithm as template method to map the filter into the conditions.
 *
 * @author u.joss
 *
 * @param <F> Type of the filter class, extending {@link SipamatoFilter}
 */
public abstract class AbstractFilterConditionMapper<F extends SipamatoFilter> implements GenericFilterConditionMapper<F> {

    /** {@inheritDoc}*/
    @Override
    public final Condition map(final F filter) {
        final List<Condition> conditions = new ArrayList<>();
        if (filter != null) {
            map(filter, conditions);
        }
        return DSL.and(conditions);
    }

    /**
     * Implement to map the actual filter attributes into the provided list of conditions.
     *
     * @param conditions
     */
    protected abstract void map(F filter, List<Condition> conditions);

}
