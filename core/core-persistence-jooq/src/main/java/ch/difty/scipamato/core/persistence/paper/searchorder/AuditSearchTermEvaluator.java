package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.MatchType;
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.Token;
import ch.difty.scipamato.core.persistence.ConditionalSupplier;

/**
 * {@link SearchTermEvaluator} implementation evaluating audit searchTerms.
 *
 * @author u.joss
 */
public class AuditSearchTermEvaluator implements SearchTermEvaluator<AuditSearchTerm> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Date range pattern, e.g. '2017-07-05 10:15:23-2017-07-05 17:12:33' -> 39
    // chars
    private static final int DATE_RANGE_PATTERN_LENGTH = 39;

    @NotNull
    @Override
    public Condition evaluate(@NotNull final AuditSearchTerm searchTerm) {
        final ConditionalSupplier conditions = new ConditionalSupplier();

        for (final Token token : searchTerm.getTokens()) {
            if (token.getType().matchType == MatchType.CONTAINS) {
                handleUserField(searchTerm, conditions, token);
            } else if (token.getType().matchType != MatchType.NONE) {
                handleDateField(searchTerm, conditions, token);
            }
        }
        return conditions.combineWithAnd();
    }

    private void handleUserField(@NotNull final AuditSearchTerm searchTerm, @NotNull final ConditionalSupplier conditions,
        @NotNull final Token token) {
        final String fieldName = searchTerm.getFieldName();
        checkField(fieldName, CREATED_BY, LAST_MOD_BY, "user", "CONTAINS");

        final Field<Object> field = DSL.field(fieldName);
        final String userName = "%" + Objects
            .requireNonNull(token.getUserSqlData())
            .toLowerCase() + "%";
        final SelectConditionStep<Record1<Long>> step = DSL
            .select(PAPER.ID)
            .from(PAPER)
            .innerJoin(SCIPAMATO_USER)
            .on(field.eq(SCIPAMATO_USER.ID))
            .where(DSL
                .lower(SCIPAMATO_USER.USER_NAME)
                .like(userName));
        conditions.add(() -> PAPER.ID.in(step));
    }

    private void checkField(@NotNull final String fieldName, @NotNull final Paper.PaperFields createdField,
        @NotNull final Paper.PaperFields lastModField, @NotNull final String fieldType, @NotNull final String matchType) {
        if (!isOneOrTheOther(createdField, lastModField, fieldName))
            throwWithMessage(fieldName, fieldType, createdField, lastModField, matchType);
    }

    private boolean isOneOrTheOther(@NotNull final Paper.PaperFields createdField, @NotNull final Paper.PaperFields lastModField,
        @NotNull final String fieldName) {
        //@formatter:off
        return (
               createdField.getFieldName().equals(fieldName)
            || lastModField.getFieldName().equals(fieldName)
        );
        //@formatter:on
    }

    private void throwWithMessage(@NotNull final String fieldName, @NotNull final String fieldType, @NotNull final FieldEnumType fld1,
        @NotNull final FieldEnumType fld2, @NotNull final String matchType) {
        final String msg = String.format(Locale.US, "Field %s is not one of the expected %s fields [%s, %s] entitled to use MatchType.%s", fieldName,
            fieldType, fld1.getFieldName(), fld2.getFieldName(), matchType);
        throw new IllegalArgumentException(msg);
    }

    private void handleDateField(@NotNull final AuditSearchTerm searchTerm, @NotNull final ConditionalSupplier conditions,
        @NotNull final Token token) {
        final String fieldName = searchTerm.getFieldName();
        checkField(fieldName, CREATED, LAST_MOD, "date", token.getType().matchType.name());

        if (Objects
                .requireNonNull(token.getDateSqlData())
                .length() == DATE_RANGE_PATTERN_LENGTH) {
            handleDateRange(conditions, token, fieldName);
        } else {
            handleSingleDate(conditions, token, fieldName);
        }
    }

    private void handleDateRange(@NotNull final ConditionalSupplier conditions, @NotNull final Token token, @NotNull final String fieldName) {
        final LocalDateTime ldt1 = LocalDateTime.parse(Objects
            .requireNonNull(token.getDateSqlData())
            .substring(0, 19), DateTimeFormatter.ofPattern(DATE_FORMAT));
        final LocalDateTime ldt2 = LocalDateTime.parse(token
            .getDateSqlData()
            .substring(20), DateTimeFormatter.ofPattern(DATE_FORMAT));
        addToConditions(token, DSL.field(fieldName), DSL.val(Timestamp.valueOf(ldt1)), DSL.val(Timestamp.valueOf(ldt2)), conditions);
    }

    private void handleSingleDate(@NotNull final ConditionalSupplier conditions, @NotNull final Token token, @NotNull final String fieldName) {
        final LocalDateTime ldt = LocalDateTime.parse(Objects.requireNonNull(token.getDateSqlData()), DateTimeFormatter.ofPattern(DATE_FORMAT));
        addToConditions(token, DSL.field(fieldName), DSL.val(Timestamp.valueOf(ldt)), DSL.val(Timestamp.valueOf(ldt)), conditions);
    }

    private void addToConditions(@NotNull final Token token, @NotNull final Field<Object> field, @NotNull final Field<Timestamp> value1,
        @NotNull final Field<Timestamp> value2, @NotNull final ConditionalSupplier conditions) {
        switch (token.getType().matchType) {
        case RANGE:
            conditions.add(() -> field.between(value1, value2));
            break;
        case GREATER_THAN:
            conditions.add(() -> field.greaterThan(value1));
            break;
        case GREATER_OR_EQUAL:
            conditions.add(() -> field.greaterOrEqual(value1));
            break;
        case LESS_OR_EQUAL:
            conditions.add(() -> field.lessOrEqual(value1));
            break;
        case LESS_THAN:
            conditions.add(() -> field.lessThan(value1));
            break;
        // Equals
        default:
            conditions.add(() -> field.equal(value1));
            break;
        }
    }
}
