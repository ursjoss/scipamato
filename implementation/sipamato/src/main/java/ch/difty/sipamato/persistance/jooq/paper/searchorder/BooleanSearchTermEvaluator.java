package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.filter.BooleanSearchTerm;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.lib.TranslationUtils;

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
        final String fieldName = TranslationUtils.deCamelCase(searchTerm.getFieldName());
        return DSL.field(fieldName).equal(DSL.val(searchTerm.getValue()));
    }

}
