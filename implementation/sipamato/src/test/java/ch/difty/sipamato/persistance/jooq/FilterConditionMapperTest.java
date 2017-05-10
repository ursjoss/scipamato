package ch.difty.sipamato.persistance.jooq;

import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Record;
import org.jooq.impl.TableImpl;
import org.junit.Test;

import ch.difty.sipamato.entity.filter.SipamatoFilter;

public abstract class FilterConditionMapperTest<R extends Record, TI extends TableImpl<R>, F extends SipamatoFilter> {

    protected String makeWhereClause(String pattern, String... fieldNames) {
        int fields = fieldNames.length;
        final StringBuilder sb = new StringBuilder();
        sb.append("(").append("\n  ");
        for (final String fieldName : fieldNames) {
            sb.append("lower(\"public\".\"").append(getTable().getName()).append("\".\"").append(fieldName).append("\") like lower('%").append(pattern).append("%')").append("\n");
            if (fields-- > 1)
                sb.append("  or ");
        }
        return sb.append(")").toString();
    }

    /**
     * @return the jOOQ generated table of type {@code T}
     */
    protected abstract TI getTable();

    @Test
    public void mappingFilter_withNullFilter_returnsNoOpCondition() {
        assertThat(getMapper().map(null).toString()).isEqualTo("1 = 1");
    }

    protected abstract GenericFilterConditionMapper<F> getMapper();

    @Test
    public void creatingWhereCondition_withNoFilterConditions_returnsNoOpCondition() {
        assertThat(getMapper().map(getFilter()).toString()).isEqualTo("1 = 1");
    }

    protected abstract F getFilter();

}
