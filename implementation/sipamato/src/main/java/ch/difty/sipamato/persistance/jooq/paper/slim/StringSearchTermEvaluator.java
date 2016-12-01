package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.filter.StringSearchTerm;
import ch.difty.sipamato.lib.AssertAs;

/**
 * {@link SearchTermEvaluator} implementation evaluating string searchTerms.
 *
 * @author u.joss
 */
class StringSearchTermEvaluator implements SearchTermEvaluator<StringSearchTerm> {

    /** {@inheritDoc} */
    @Override
    public Condition evaluate(final StringSearchTerm searchTerm) {
        AssertAs.notNull(searchTerm, "searchTerm");

        switch (searchTerm.getType()) {
        case EXACT:
            return DSL.field(searchTerm.getKey()).lower().equal(DSL.val(searchTerm.getValue()).lower());
        case CONTAINS:
            return DSL.field(searchTerm.getKey()).lower().contains(DSL.val(searchTerm.getValue()).lower());
        case STARTS_WITH:
            return DSL.field(searchTerm.getKey()).lower().startsWith(DSL.val(searchTerm.getValue()).lower());
        case ENDS_WITH:
            return DSL.field(searchTerm.getKey()).lower().endsWith(DSL.val(searchTerm.getValue()).lower());
        default:
            throw new UnsupportedOperationException("Evaluation of type " + searchTerm.getType() + " is not supported...");
        }
    }

}
