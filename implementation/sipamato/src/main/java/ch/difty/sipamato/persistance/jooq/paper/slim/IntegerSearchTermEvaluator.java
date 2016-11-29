package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.IntegerSearchTerm;
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
            return DSL.field(searchTerm.getKey()).equal(DSL.val(searchTerm.getValue()));
        case LESS_OR_EQUAL:
            return DSL.field(searchTerm.getKey()).le(DSL.val(searchTerm.getValue()));
        case LESS_THAN:
            return DSL.field(searchTerm.getKey()).lt(DSL.val(searchTerm.getValue()));
        case GREATER_OR_EQUAL:
            return DSL.field(searchTerm.getKey()).ge(DSL.val(searchTerm.getValue()));
        case GREATER_THAN:
            return DSL.field(searchTerm.getKey()).gt(DSL.val(searchTerm.getValue()));
        case RANGE:
            return DSL.field(searchTerm.getKey()).between(DSL.val(searchTerm.getValue()), DSL.val(searchTerm.getValue2()));
        default:
            throw new UnsupportedOperationException("Evaluation of type " + searchTerm.getType() + " is not supported...");
        }
    }

}
