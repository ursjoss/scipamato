package ch.difty.scipamato.persistence.paper.searchorder;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.AssertAs;
import ch.difty.scipamato.TranslationUtils;
import ch.difty.scipamato.entity.filter.BooleanSearchTerm;

/**
 * {@link SearchTermEvaluator} implementation evaluating boolean searchTerms.
 *
 * @author u.joss
 */
public class BooleanSearchTermEvaluator implements SearchTermEvaluator<BooleanSearchTerm> {

    /** {@inheritDoc} */
    @Override
    public Condition evaluate(final BooleanSearchTerm searchTerm) {
        AssertAs.notNull(searchTerm, "searchTerm");
        final String fieldName = TranslationUtils.deCamelCase(searchTerm.getFieldName());
        return DSL.field(fieldName).equal(DSL.val(searchTerm.getValue()));
    }

}
