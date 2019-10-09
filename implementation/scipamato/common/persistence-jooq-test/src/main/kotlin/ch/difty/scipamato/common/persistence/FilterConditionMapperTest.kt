package ch.difty.scipamato.common.persistence

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter
import org.assertj.core.api.Assertions.assertThat
import org.jooq.Record
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.Test

@Suppress("FunctionName")
abstract class FilterConditionMapperTest<R : Record, TI : TableImpl<R>, F : ScipamatoFilter> {

    protected abstract val table: TI

    protected abstract val mapper: GenericFilterConditionMapper<F>

    protected abstract val filter: F

    protected fun makeWhereClause(pattern: String, vararg fieldNames: String): String {
        var fields = fieldNames.size
        val withMultipleFields = fields > 1
        val sb = StringBuilder()
        if (withMultipleFields) sb.append("(").append("\n  ")
        fieldNames.forEach { fieldName ->
            sb.append("lower(\"public\".\"").append(table.name).append("\".\"").append(fieldName)
                .append("\") like lower('%").append(pattern).append("%')")
            if (withMultipleFields) sb.append("\n")
            if (fields-- > 1) sb.append("  or ")
        }
        if (withMultipleFields) sb.append(")")
        return sb.toString()
    }

    @Test
    internal fun mappingFilter_withNullFilter_returnsNoOpCondition() {
        assertThat(mapper.map(null).toString()).isEqualTo("1 = 1")
    }

    @Test
    internal fun creatingWhereCondition_withNoFilterConditions_returnsNoOpCondition() {
        assertThat(mapper.map(filter).toString()).isEqualTo("1 = 1")
    }
}
