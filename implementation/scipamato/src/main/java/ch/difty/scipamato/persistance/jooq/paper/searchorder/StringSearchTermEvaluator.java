package ch.difty.scipamato.persistance.jooq.paper.searchorder;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import ch.difty.scipamato.entity.filter.StringSearchTerm;
import ch.difty.scipamato.entity.filter.StringSearchTerm.Token;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.lib.TranslationUtils;
import ch.difty.scipamato.persistance.jooq.ConditionalSupplier;

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

        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final Token token : searchTerm.getTokens()) {
            final String fieldName = TranslationUtils.deCamelCase(searchTerm.getFieldName());
            addToConditions(conditions, token, DSL.field(fieldName), DSL.val(token.sqlData));
        }
        return conditions.combineWithAnd();
    }

    private void addToConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field, final Field<String> value) {
        final Field<String> fieldLowered = field.lower();
        final Field<String> valueLowered = value.lower();
        final boolean negate = tk.type.negate;
        switch (tk.type.matchType) {
        case NONE:
            break;
        case CONTAINS:
            cs.add(() -> negate ? DSL.not(fieldLowered.contains(valueLowered)) : fieldLowered.contains(valueLowered));
            break;
        case EQUALS:
            cs.add(() -> negate ? fieldLowered.notEqual(valueLowered) : fieldLowered.equal(valueLowered));
            break;
        case LIKE:
            cs.add(() -> negate ? fieldLowered.notLike(valueLowered) : fieldLowered.like(valueLowered));
            break;
        case REGEX:
            cs.add(() -> negate ? field.notLikeRegex(value) : field.likeRegex(value));
            break;
        case LENGTH:
            final Field<Integer> length = field.length();
            cs.add(() -> negate ? field.isNull().or(length.equal(0)) : field.isNotNull().and(length.greaterThan(0)));
            break;
        default:
            throw new UnsupportedOperationException("Evaluation of type " + tk.type.matchType + " is not supported...");
        }
    }
}
