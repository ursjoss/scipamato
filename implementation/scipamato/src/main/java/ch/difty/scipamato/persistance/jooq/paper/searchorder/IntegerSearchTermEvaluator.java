package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.impl.DSL;

import ch.difty.scipamato.entity.filter.IntegerSearchTerm;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.lib.TranslationUtils;

/**
 * {@link SearchTermEvaluator} implementation evaluating integer searchTerms.
 *
 * @author u.joss
 */
class IntegerSearchTermEvaluator implements SearchTermEvaluator<IntegerSearchTerm> {

    /** {@inheritDoc} */
    @Override
    public Condition evaluate(final IntegerSearchTerm searchTerm) {
        AssertAs.notNull(searchTerm, "searchTerm");

        final String fieldName = TranslationUtils.deCamelCase(searchTerm.getFieldName());
        final Field<Object> field = DSL.field(fieldName);
        final Param<Integer> value = DSL.val(searchTerm.getValue());

        switch (searchTerm.getType()) {
        case EXACT:
            return field.equal(value);
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
        default:
            throw new UnsupportedOperationException("Evaluation of type " + searchTerm.getType() + " is not supported...");
        }
    }

}
