package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.BooleanSearchTerm;
import ch.difty.sipamato.lib.AssertAs;

/**
 * {@link SearchTermEvaluator} implementation evaluating boolean searchTerms.
 *
 * @author u.joss
 */
class BooleanSearchTermEvaluator implements SearchTermEvaluator<BooleanSearchTerm> {

    /** {@inheritDoc} */
    @Override
    public Condition evaluate(final BooleanSearchTerm searchTerm) {
        AssertAs.notNull(searchTerm, "searchTerm");
        return DSL.field(searchTerm.getKey()).equal(DSL.val(searchTerm.getValue()));
    }

}
