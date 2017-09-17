package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.entity.filter.BooleanSearchTerm;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.lib.TranslationUtils;

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
