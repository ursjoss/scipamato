package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.ScipamatoEntity
import ch.difty.scipamato.common.persistence.paging.Sort
import org.jooq.Record
import org.jooq.SortField
import org.jooq.impl.TableImpl

/**
 * Implementations of this interface map sorting specifications into the jOOQ specific [SortField]s.
 *
 * @param [R] the type of the record, extending [Record]
 * @param [T] the type of the entity, extending [ScipamatoEntity]
 * @param [T] the type of the table implementation of record [R]
 */
fun interface JooqSortMapper<R : Record, T : ScipamatoEntity, TI : TableImpl<R>> {

    /**
     * Maps spring data [sortSpecification] of [table] [TI] into the jOOQ specific [SortField]s.
     */
    fun map(sortSpecification: Sort?, table: TI): Collection<SortField<T>>
}
