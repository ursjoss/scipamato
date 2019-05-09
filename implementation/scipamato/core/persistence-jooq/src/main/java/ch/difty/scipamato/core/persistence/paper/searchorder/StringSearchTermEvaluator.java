package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.AssertAs;
import ch.difty.scipamato.common.TranslationUtils;
import ch.difty.scipamato.core.entity.search.StringSearchTerm;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.Token;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;

/**
 * {@link SearchTermEvaluator} implementation evaluating string searchTerms.
 *
 * @author u.joss
 */
public class StringSearchTermEvaluator implements SearchTermEvaluator<StringSearchTerm> {

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

    private void addToConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field,
        final Field<String> value) {
        distinguishConditions(cs, tk, field, value, field.lower(), value.lower(), tk.type.negate);
    }

    private void distinguishConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field,
        final Field<String> value, final Field<String> flc, final Field<String> vlc, final boolean negate) {
        switch (tk.type.matchType) {
        case NONE:
            break;
        case CONTAINS:
            containsCondition(cs, flc, vlc, negate);
            break;
        case EQUALS:
            equalsCondition(cs, flc, vlc, negate);
            break;
        case LIKE:
            likeCondition(cs, flc, vlc, negate);
            break;
        case REGEX:
            regexCondition(cs, field, value, negate);
            break;
        case LENGTH:
            lengthCondition(cs, field, negate);
            break;
        case UNSUPPORTED:
        default:
            throw new AssertionError("Evaluation of type " + tk.type.matchType + " is not supported...");
        }
    }

    private void containsCondition(final ConditionalSupplier cs, final Field<String> flc, final Field<String> vlc,
        final boolean negate) {
        cs.add(() -> negate ? DSL.not(flc.contains(vlc)) : flc.contains(vlc));
    }

    private void equalsCondition(final ConditionalSupplier cs, final Field<String> flc, final Field<String> vlc,
        final boolean negate) {
        cs.add(() -> negate ? flc.notEqual(vlc) : flc.equal(vlc));
    }

    private void likeCondition(final ConditionalSupplier cs, final Field<String> flc, final Field<String> vlc,
        final boolean negate) {
        cs.add(() -> negate ? flc.notLike(vlc) : flc.like(vlc));
    }

    private void regexCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value,
        final boolean negate) {
        cs.add(() -> negate ? field.notLikeRegex(value) : field.likeRegex(value));
    }

    private void lengthCondition(final ConditionalSupplier cs, final Field<Object> field, final boolean negate) {
        final Field<Integer> length = field.length();
        cs.add(() -> negate ?
            field
                .isNull()
                .or(length.equal(0)) :
            field
                .isNotNull()
                .and(length.greaterThan(0)));
    }
}
