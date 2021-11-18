package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.common.persistence.TranslationUtilsKt.deCamelCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.persistence.TranslationUtilsKt;
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
@SuppressWarnings("SpellCheckingInspection")
public class StringSearchTermEvaluator implements SearchTermEvaluator<StringSearchTerm> {

    private static final String METHODS    = "methods";
    private static final String POPULATION = "population";

    private final List<Field<Object>> methodFields     = new ArrayList<>();
    private final List<Field<Object>> populationFields = new ArrayList<>();

    public StringSearchTermEvaluator() {
        methodFields.addAll(Stream
            .of(METHODS, "methodStudyDesign", "populationPlace", "methodOutcome", "exposurePollutant", "exposureAssessment", "methodStatistics",
                "methodConfounders")
            .map(TranslationUtilsKt::deCamelCase)
            .map(DSL::field)
            .collect(Collectors.toList()));
        populationFields.addAll(Stream
            .of(POPULATION, "populationPlace", "populationParticipants", "populationDuration")
            .map(TranslationUtilsKt::deCamelCase)
            .map(DSL::field)
            .collect(Collectors.toList()));
    }

    @NotNull
    @Override
    public Condition evaluate(@NotNull final StringSearchTerm searchTerm) {
        final ConditionalSupplier conditions = new ConditionalSupplier();
        for (final Token token : searchTerm.getTokens()) {
            final String fieldName = deCamelCase(searchTerm.getFieldName());
            addToConditions(conditions, token, DSL.field(fieldName), DSL.val(token.sqlData));
        }
        return conditions.combineWithAnd();
    }

    private void addToConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field, final Field<String> value) {
        distinguishConditions(cs, tk, field, value, tk.type.negate);
    }

    private void distinguishConditions(final ConditionalSupplier cs, final Token tk, final Field<Object> field, final Field<String> value,
        final boolean negate) {
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

    private void containsCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            containsMultiple(cs, value, negate, methodFields);
        } else if (POPULATION.equalsIgnoreCase(field.getName())) {
            containsMultiple(cs, value, negate, populationFields);
        } else {
            cs.add(() -> negate ?
                DSL
                    .coalesce(field, "")
                    .notContainsIgnoreCase(value) :
                field.containsIgnoreCase(value));
        }
    }

    private void containsMultiple(final ConditionalSupplier cs, final Field<String> value, final boolean negate, final List<Field<Object>> fields) {
        ConditionalSupplier csSub = new ConditionalSupplier();
        for (final Field<Object> mf : fields)
            csSub.add(() -> negate ?
                DSL
                    .coalesce(mf, "")
                    .notContainsIgnoreCase(value) :
                mf.containsIgnoreCase(value));
        cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
    }

    private void equalsCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            equalsMultiple(cs, value, negate, methodFields);
        } else if (POPULATION.equalsIgnoreCase(field.getName())) {
            equalsMultiple(cs, value, negate, populationFields);
        } else {
            cs.add(() -> negate ? field.notEqualIgnoreCase(value) : field.equalIgnoreCase(value));
        }
    }

    private void equalsMultiple(final ConditionalSupplier cs, final Field<String> value, final boolean negate, final List<Field<Object>> fields) {
        ConditionalSupplier csSub = new ConditionalSupplier();
        for (final Field<Object> mf : fields)
            csSub.add(() -> negate ? mf.notEqualIgnoreCase(value) : mf.equalIgnoreCase(value));
        cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
    }

    private void likeCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            likeMultiple(cs, value, negate, methodFields);
        } else if (POPULATION.equalsIgnoreCase(field.getName())) {
            likeMultiple(cs, value, negate, populationFields);
        } else {
            cs.add(() -> negate ?
                DSL
                    .coalesce(field, "")
                    .notLikeIgnoreCase(value) :
                field.likeIgnoreCase(value));
        }
    }

    private void likeMultiple(final ConditionalSupplier cs, final Field<String> value, final boolean negate, final List<Field<Object>> fields) {
        ConditionalSupplier csSub = new ConditionalSupplier();
        for (final Field<Object> mf : fields) {
            csSub.add(() -> negate ?
                DSL
                    .coalesce(mf, "")
                    .notLikeIgnoreCase(value) :
                mf.likeIgnoreCase(value));
        }
        cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
    }

    private void regexCondition(final ConditionalSupplier cs, final Field<Object> field, final Field<String> value, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            regexMultiple(cs, value, negate, methodFields);
        } else if (POPULATION.equalsIgnoreCase(field.getName())) {
            regexMultiple(cs, value, negate, populationFields);
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

    private void regexMultiple(final ConditionalSupplier cs, final Field<String> value, final boolean negate, final List<Field<Object>> fields) {
        ConditionalSupplier csSub = new ConditionalSupplier();
        for (final Field<Object> mf : fields) {
            csSub.add(() -> negate ?
                DSL
                    .lower(DSL
                        .coalesce(mf, "")
                        .cast(String.class))
                    .notLikeRegex(value) :
                DSL
                    .lower(DSL
                        .coalesce(mf, "")
                        .cast(String.class))
                    .likeRegex(value));
        }
        cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
    }

    private void lengthCondition(final ConditionalSupplier cs, final Field<Object> field, final boolean negate) {
        if (METHODS.equalsIgnoreCase(field.getName())) {
            lengthMultiple(cs, negate, methodFields);
        } else if (POPULATION.equalsIgnoreCase(field.getName())) {
            lengthMultiple(cs, negate, populationFields);
        } else {
            lengthCond(cs, field, negate);
        }
    }

    private void lengthMultiple(final ConditionalSupplier cs, final boolean negate, final List<Field<Object>> fields) {
        ConditionalSupplier csSub = new ConditionalSupplier();
        for (final Field<Object> mf : fields) {
            lengthCond(csSub, mf, negate);
        }
        cs.add(() -> negate ? csSub.combineWithAnd() : csSub.combineWithOr());
    }

    private void lengthCond(final ConditionalSupplier cs, final Field<Object> field, final boolean negate) {
        final Field<Integer> length = DSL.length(field.cast(String.class));
        cs.add(() -> negate ?
            field
                .isNull()
                .or(length.equal(0)) :
            field
                .isNotNull()
                .and(length.greaterThan(0)));
    }
}
