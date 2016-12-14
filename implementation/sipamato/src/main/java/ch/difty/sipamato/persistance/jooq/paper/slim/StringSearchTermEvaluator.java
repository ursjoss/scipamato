package ch.difty.sipamato.persistance.jooq.paper.slim;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.filter.StringSearchTerm;
import ch.difty.sipamato.entity.filter.StringSearchTerm.Token;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.ConditionalSupplier;

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
            final Field<Object> field = DSL.field(searchTerm.getFieldName());
            final Field<String> fieldLowered = field.lower();
            final Field<String> value = DSL.val(token.sqlData);
            final Field<String> valueLowered = value.lower();
            final boolean negate = token.type.negate;
            switch (token.type.matchType) {
            case NONE:
                break;
            case CONTAINS:
                conditions.add(() -> (negate ? DSL.not(fieldLowered.contains(valueLowered)) : fieldLowered.contains(valueLowered)));
                break;
            case EQUALS:
                conditions.add(() -> (negate ? fieldLowered.notEqual(valueLowered) : fieldLowered.equal(valueLowered)));
                break;
            case LIKE:
                conditions.add(() -> (negate ? fieldLowered.notLike(valueLowered) : fieldLowered.like(valueLowered)));
                break;
            case REGEX:
                conditions.add(() -> (negate ? field.notLikeRegex(value) : field.likeRegex(value)));
                break;
            case LENGTH:
                final Field<Integer> length = field.length();
                conditions.add(() -> (negate ? length.equal(0) : length.greaterThan(0)));
                break;
            default:
                throw new UnsupportedOperationException("Evaluation of type " + token.type.matchType + " is not supported...");
            }
        }
        return conditions.combineWithAnd();
    }
}
