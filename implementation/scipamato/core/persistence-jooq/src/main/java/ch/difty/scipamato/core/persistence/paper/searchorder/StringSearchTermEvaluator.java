package ch.difty.scipamato.core.persistence.paper.searchorder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
 * <p>
 * Note: Searching field methods results in searching associated short study fields too.
 *
 * @author u.joss
 */
public class StringSearchTermEvaluator implements SearchTermEvaluator<StringSearchTerm> {

    private static final String METHODS = "methods";

    private final List<Field<Object>> methodFields = new ArrayList<>();

    public StringSearchTermEvaluator() {
        methodFields.addAll(Stream
            .of(METHODS, "methodStudyDesign", "populationPlace", "methodOutcome", "exposurePollutant",
                "exposureAssessment", "methodStatistics", "methodConfounders")
            .map(TranslationUtils.INSTANCE::deCamelCase)
            .map(DSL::field)
            .collect(Collectors.toList()));
    }

    @Override
    public Condition evaluate(final StringSearchTerm searchTerm) {
        AssertAs.INSTANCE.notNull(searchTerm, "searchTerm");

        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final Token token : searchTerm.getTokens()) {
            final String fieldName = TranslationUtils.INSTANCE.deCamelCase(searchTerm.getFieldName());
            addToConditions(conditions, token, DSL.field(fieldName), DSL.val(token.sqlData));
        }
        return conditions.combineWithAnd();
    }

    private void addToConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field,
        final Field<String> value) {
        distinguishConditions(cs, tk, field, value, tk.type.negate);
    }

    private void distinguishConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field,
        final Field<String> value, final boolean negate) {
        switch (tk.type.matchType) {
        case NONE:
            break;
        case CONTAINS:
            containsCondition(cs, field, value, negate);
            break;
        case EQUALS:
            equalsCondition(cs, field, value, negate);
            break;
        case LIKE:
            likeCondition(cs, field, value, negate);
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

    private void containsCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value,
        final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            ConditionalSupplier csSub = new ConditionalSupplier();
            for (final Field<Object> mf : methodFields)
                csSub.add(() -> negate ?
                    DSL
                        .coalesce(mf, "")
                        .notContainsIgnoreCase(value) :
                    mf.containsIgnoreCase(value));
            cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
        } else {
            cs.add(() -> negate ?
                DSL
                    .coalesce(field, "")
                    .notContainsIgnoreCase(value) :
                field.containsIgnoreCase(value));
        }
    }

    private void equalsCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value,
        final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            ConditionalSupplier csSub = new ConditionalSupplier();
            for (final Field<Object> mf : methodFields)
                csSub.add(() -> negate ? mf.notEqualIgnoreCase(value) : mf.equalIgnoreCase(value));
            cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
        } else {
            cs.add(() -> negate ? field.notEqualIgnoreCase(value) : field.equalIgnoreCase(value));
        }
    }

    private void likeCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value,
        final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            ConditionalSupplier csSub = new ConditionalSupplier();
            for (final Field<Object> mf : methodFields) {
                csSub.add(() -> negate ?
                    DSL
                        .coalesce(mf, "")
                        .notLikeIgnoreCase(value) :
                    mf.likeIgnoreCase(value));
            }
            cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
        } else {
            cs.add(() -> negate ?
                DSL
                    .coalesce(field, "")
                    .notLikeIgnoreCase(value) :
                field.likeIgnoreCase(value));
        }
    }

    private void regexCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value,
        final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            ConditionalSupplier csSub = new ConditionalSupplier();
            for (final Field mf : methodFields) {
                //noinspection unchecked
                csSub.add(() -> negate ?
                    DSL
                        .coalesce(mf, "")
                        .lower()
                        .notLikeRegex(value) :
                    DSL
                        .coalesce(mf, "")
                        .lower()
                        .likeRegex(value));
            }
            cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
        } else {
            cs.add(() -> negate ?
                DSL
                    .coalesce(field, "")
                    .notLikeRegex(value) :
                DSL
                    .coalesce(field, "")
                    .likeRegex(value));
        }
    }

    private void lengthCondition(final ConditionalSupplier cs, final Field<Object> field, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            ConditionalSupplier csSub = new ConditionalSupplier();
            for (final Field<Object> mf : methodFields) {
                lengthCond(csSub, mf, negate);
            }
            cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
        } else {
            lengthCond(cs, field, negate);
        }
    }

    private void lengthCond(final ConditionalSupplier cs, final Field<Object> field, final boolean negate) {
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
