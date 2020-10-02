package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.common.persistence.TranslationUtilsKt.deCamelCase;

import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm;

/**
 * {@link SearchTermEvaluator} implementation evaluating boolean searchTerms.
 *
 * @author u.joss
 */
public class BooleanSearchTermEvaluator implements SearchTermEvaluator<BooleanSearchTerm> {

    @NotNull
    @Override
    public Condition evaluate(@NotNull final BooleanSearchTerm searchTerm) {
        final String fieldName = deCamelCase(searchTerm.getFieldName());
        return DSL
            .field(fieldName)
            .equal(DSL.val(searchTerm.getValue()));
    }
}
