package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Mapper translating the provided filter into matching conditions.
 *
 * @param <F> Filter class extending [ScipamatoFilter]
 */
interface GenericFilterConditionMapper<in F : ScipamatoFilter> {

    /**
     * Map the provided [filter] (extending [ScipamatoFilter]) into a jOOQ [Condition]
     */
    fun map(filter: F?): Condition
}
