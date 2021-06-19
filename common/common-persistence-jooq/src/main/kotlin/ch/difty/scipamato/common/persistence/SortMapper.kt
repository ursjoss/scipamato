package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.DbEntity
import ch.difty.scipamato.common.entity.ScipamatoEntity
import ch.difty.scipamato.common.persistence.paging.Sort
import org.jooq.Record
import org.jooq.SortField
import org.jooq.TableField
import org.jooq.impl.TableImpl
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Component
import java.util.*

/**
 * Default implementation of the [JooqSortMapper] interface.
 *
 * Sort properties are de-camel- and upper-cased; java property names are
 * therefore converted to table column names, e.g. `publicationYear` will
 * be translated to `PUBLICATION_YEAR`.
 *
 * @param [R] the type of the record, extending [Record]
 * @param [T] the type of the entity, extending [ScipamatoEntity]
 * @param [TI] the type of the table implementation of record `R`
 */
@Component
@Deprecated("use DbSortMapper instead")
class SortMapper<R : Record, T : ScipamatoEntity, TI : TableImpl<R>> : JooqSortMapper<R, T, TI> {

    override fun map(sortSpecification: Sort?, table: TI): Collection<SortField<T>> =
        sortSpecification?.map { (propName, sortDirection) ->
            table.getField(propName).toSortField(sortDirection)
        } ?: emptyList()

    private fun TI.getField(fieldName: String): TableField<R, T> = try {
        getTableFieldFor(this, deCamelCase(fieldName).uppercase(Locale.getDefault()))
    } catch (ex: ReflectiveOperationException) {
        throw InvalidDataAccessApiUsageException("Could not find table field: $fieldName", ex)
    }

    /**
     * reflection based field extraction so we can stub it out in tests
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun getTableFieldFor(table: TI, columnName: String): TableField<R, T> =
        table::class.java.getField(columnName).get(table) as TableField<R, T>

    private fun TableField<R, T>.toSortField(sortDirection: Sort.Direction): SortField<T> =
        if (sortDirection === Sort.Direction.ASC) asc() else desc()
}

@Component
class DbSortMapper<R : Record, T : DbEntity, TI : TableImpl<R>> : JooqDbSortMapper<R, T, TI> {

    override fun map(sortSpecification: Sort?, table: TI): Collection<SortField<T>> =
        sortSpecification?.map { (propName, sortDirection) ->
            table.getField(propName).toSortField(sortDirection)
        } ?: emptyList()

    private fun TI.getField(fieldName: String): TableField<R, T> = try {
        getTableFieldFor(this, deCamelCase(fieldName).uppercase(Locale.getDefault()))
    } catch (ex: ReflectiveOperationException) {
        throw InvalidDataAccessApiUsageException("Could not find table field: $fieldName", ex)
    }

    /**
     * reflection based field extraction so we can stub it out in tests
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun getTableFieldFor(table: TI, columnName: String): TableField<R, T> =
        table::class.java.getField(columnName).get(table) as TableField<R, T>

    private fun TableField<R, T>.toSortField(sortDirection: Sort.Direction): SortField<T> =
        if (sortDirection === Sort.Direction.ASC) asc() else desc()
}
