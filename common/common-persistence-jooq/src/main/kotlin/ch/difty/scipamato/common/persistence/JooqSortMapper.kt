package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.DbEntity
import ch.difty.scipamato.common.entity.ScipamatoEntity
import ch.difty.scipamato.common.persistence.paging.Sort
import org.jooq.Record
import org.jooq.SortField
import org.jooq.Table

/**
 * Implementations of this interface map sorting specifications into the jOOQ specific [SortField]s.
 *
 * @param [R] the type of the record, extending [Record]
 * @param [T] the type of the entity, extending [ScipamatoEntity]
 * @param [T] the type of the table implementation of record [R]
 */
@Deprecated("replace with JooqDbSortMapper")
fun interface JooqSortMapper<R : Record, T : ScipamatoEntity, TI : Table<R>> {

    /**
     * Maps spring data [sortSpecification] of [table] [TI] into the jOOQ specific [SortField]s.
     */
    fun map(sortSpecification: Sort?, table: TI): Collection<SortField<T>>
}

/**
 * Implementations of this interface map sorting specifications into the jOOQ specific [SortField]s.
 *
 * @param [R] the type of the record, extending [Record]
 * @param [T] the type of the entity, extending [DbEntity]
 * @param [T] the type of the table implementation of record [R]
 */
fun interface JooqDbSortMapper<R : Record, T : DbEntity, TI : Table<R>> {

    /**
     * Maps spring data [sortSpecification] of [table] [TI] into the jOOQ specific [SortField]s.
     */
    fun map(sortSpecification: Sort?, table: TI): Collection<SortField<T>>
}
