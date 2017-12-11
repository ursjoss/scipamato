package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.core.entity.filter.BooleanSearchTerm;

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
