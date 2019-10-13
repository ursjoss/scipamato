package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm;

/**
 * {@link SearchTermEvaluator} implementation evaluating integer searchTerms.
 *
 * @author u.joss
 */
public class IntegerSearchTermEvaluator implements SearchTermEvaluator<IntegerSearchTerm> {

    @NotNull
    @Override
    public Condition evaluate(@NotNull final IntegerSearchTerm searchTerm) {
        final String fieldName = getFieldName(searchTerm.getFieldName());
        final Field<Object> field = DSL.field(fieldName);
        final Param<Integer> value = DSL.val(searchTerm.getValue());

        switch (searchTerm.getType()) {
        case LESS_OR_EQUAL:
            return field.le(value);
        case LESS_THAN:
            return field.lt(value);
        case GREATER_OR_EQUAL:
            return field.ge(value);
        case GREATER_THAN:
            return field.gt(value);
        case RANGE:
            return field.between(value, DSL.val(searchTerm.getValue2()));
        case MISSING:
            return field.isNull();
        case PRESENT:
            return field.isNotNull();
        case INCOMPLETE:
            return DSL.falseCondition();
        default:
            return field.equal(value);
        }
    }

    private String getFieldName(final String fieldName) {
        if ("id".equals(fieldName))
            return "paper.id";
        else
            return TranslationUtils.INSTANCE.deCamelCase(fieldName);
    }
}
