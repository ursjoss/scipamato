package ch.difty.scipamato.common.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Record;
import org.jooq.impl.TableImpl;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;

public abstract class FilterConditionMapperTest<R extends Record, TI extends TableImpl<R>, F extends ScipamatoFilter> {

    protected String makeWhereClause(String pattern, String... fieldNames) {
        int fields = fieldNames.length;
        final boolean withMultipleFields = fields > 1;
        final StringBuilder sb = new StringBuilder();
        if (withMultipleFields)
            sb
                .append("(")
                .append("\n  ");
        for (final String fieldName : fieldNames) {
            sb
                .append("lower(\"public\".\"")
                .append(getTable().getName())
                .append("\".\"")
                .append(fieldName)
                .append("\") like lower('%")
                .append(pattern)
                .append("%')");
            if (withMultipleFields)
                sb.append("\n");
            if (fields-- > 1)
                sb.append("  or ");
        }
        if (withMultipleFields)
            sb.append(")");
        return sb.toString();
    }

    /**
     * @return the jOOQ generated table of type {@code T}
     */
    protected abstract TI getTable();

    @Test
    public void mappingFilter_withNullFilter_returnsNoOpCondition() {
        assertThat(getMapper()
            .map(null)
            .toString()).isEqualTo("1 = 1");
    }

    protected abstract GenericFilterConditionMapper<F> getMapper();

    @Test
    public void creatingWhereCondition_withNoFilterConditions_returnsNoOpCondition() {
        assertThat(getMapper()
            .map(getFilter())
            .toString()).isEqualTo("1 = 1");
    }

    protected abstract F getFilter();

}
