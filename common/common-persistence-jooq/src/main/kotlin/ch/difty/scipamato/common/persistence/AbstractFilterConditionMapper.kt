package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Abstract class providing the basic algorithm as template method to map the
 * filter into the conditions.
 *
 * @param <F> Type of the filter class, extending [ScipamatoFilter]
 */
abstract class AbstractFilterConditionMapper<in F : ScipamatoFilter> : GenericFilterConditionMapper<F> {

    final override fun map(filter: F?): Condition {
        val conditions = filter?.let { internalMap(it) } ?: emptyList()
        return DSL.and(conditions)
    }

    /**
     * Maps the actual [filter] attributes into the provided list of [Condition]s.
     */
    protected abstract fun internalMap(filter: F): List<Condition>
}
