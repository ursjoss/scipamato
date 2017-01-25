package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.filter.IntegerSearchTerm;
import ch.difty.sipamato.lib.AssertAs;

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

        switch (searchTerm.getType()) {
        case EXACT:
            return DSL.field(searchTerm.getFieldName()).equal(DSL.val(searchTerm.getValue()));
        case LESS_OR_EQUAL:
            return DSL.field(searchTerm.getFieldName()).le(DSL.val(searchTerm.getValue()));
        case LESS_THAN:
            return DSL.field(searchTerm.getFieldName()).lt(DSL.val(searchTerm.getValue()));
        case GREATER_OR_EQUAL:
            return DSL.field(searchTerm.getFieldName()).ge(DSL.val(searchTerm.getValue()));
        case GREATER_THAN:
            return DSL.field(searchTerm.getFieldName()).gt(DSL.val(searchTerm.getValue()));
        case RANGE:
            return DSL.field(searchTerm.getFieldName()).between(DSL.val(searchTerm.getValue()), DSL.val(searchTerm.getValue2()));
        case MISSING:
            return DSL.field(searchTerm.getFieldName()).isNull();
        case PRESENT:
            return DSL.field(searchTerm.getFieldName()).isNotNull();
        default:
            throw new UnsupportedOperationException("Evaluation of type " + searchTerm.getType() + " is not supported...");
        }
    }

}
