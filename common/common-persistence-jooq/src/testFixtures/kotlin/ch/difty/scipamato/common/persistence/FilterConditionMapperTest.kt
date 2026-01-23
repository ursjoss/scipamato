package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import org.amshove.kluent.shouldBeEqualTo
import org.jooq.Record
import org.jooq.Table
import org.junit.jupiter.api.Test

@Suppress("FunctionNaming", "kotlin:S100", "unused")
abstract class FilterConditionMapperTest<R : Record, TI : Table<R>, F : ScipamatoFilter> {

    protected abstract val table: TI

    protected abstract val mapper: GenericFilterConditionMapper<F>

    protected abstract val filter: F

    protected fun makeWhereClause(pattern: String, vararg fieldNames: String): String {
        var fields = fieldNames.size
        val withMultipleFields = fields > 1
        val sb = StringBuilder()
        if (withMultipleFields) sb.append("(").append("\n  ")
        fieldNames.forEach { fieldName ->
            sb.append("\"public\".\"").append(table.name).append("\".\"").append(fieldName)
                .append("\" ilike '%").append(pattern).append("%'")
            if (withMultipleFields) sb.append("\n")
            if (fields-- > 1) sb.append("  or ")
        }
        if (withMultipleFields) sb.append(")")
        return sb.toString()
    }

    @Test
    internal fun mappingFilter_withNullFilter_returnsNoOpCondition() {
        mapper.map(null).toString() shouldBeEqualTo "true"
    }

    @Test
    internal fun creatingWhereCondition_withNoFilterConditions_returnsNoOpCondition() {
        mapper.map(filter).toString() shouldBeEqualTo "true"
    }
}
