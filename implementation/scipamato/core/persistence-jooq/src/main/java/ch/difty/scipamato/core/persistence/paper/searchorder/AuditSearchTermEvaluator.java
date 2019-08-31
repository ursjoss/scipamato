package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.db.tables.Paper.PAPER;
import static ch.difty.scipamato.core.db.tables.ScipamatoUser.SCIPAMATO_USER;
import static ch.difty.scipamato.core.entity.Paper.PaperFields.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import ch.difty.scipamato.common.AssertAs;
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

    @Override
    public Condition evaluate(final AuditSearchTerm searchTerm) {
        AssertAs.INSTANCE.notNull(searchTerm, "searchTerm");

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

    private void handleUserField(final AuditSearchTerm searchTerm, final ConditionalSupplier conditions,
        final Token token) {
        final String fieldName = searchTerm.getFieldName();
        checkField(fieldName, CREATED_BY, LAST_MOD_BY, "user", "CONTAINS");

        final Field<Object> field = DSL.field(fieldName);
        final String userName = "%" + token
            .getUserSqlData()
            .toLowerCase() + "%";
        final SelectConditionStep<Record1<Long>> step = DSL
            .select(PAPER.ID)
            .from(PAPER)
            .innerJoin(SCIPAMATO_USER)
            .on(field.eq(SCIPAMATO_USER.ID))
            .where(SCIPAMATO_USER.USER_NAME
                .lower()
                .like(userName));
        conditions.add(() -> PAPER.ID.in(step));
    }

    private void checkField(final String fieldName, final Paper.PaperFields createdField,
        final Paper.PaperFields lastModField, final String fieldType, final String matchType) {
        if (!isOneOrTheOther(createdField, lastModField, fieldName))
            throwWithMessage(fieldName, fieldType, createdField, lastModField, matchType);
    }

    private boolean isOneOrTheOther(final Paper.PaperFields createdField, final Paper.PaperFields lastModField,
        final String fieldName) {
        //@formatter:off
        return (
               createdField.getName().equals(fieldName)
            || lastModField.getName().equals(fieldName)
        );
        //@formatter:on
    }

    private void throwWithMessage(final String fieldName, String fieldType, FieldEnumType fld1, FieldEnumType fld2,
        String matchType) {
        final String msg = String.format(
            "Field %s is not one of the expected %s fields [%s, %s] entitled to use MatchType.%s", fieldName, fieldType,
            fld1.getName(), fld2.getName(), matchType);
        throw new IllegalArgumentException(msg);
    }

    private void handleDateField(final AuditSearchTerm searchTerm, final ConditionalSupplier conditions,
        final Token token) {
        final String fieldName = searchTerm.getFieldName();
        checkField(fieldName, CREATED, LAST_MOD, "date", token.getType().matchType.name());

        if (token
                .getDateSqlData()
                .length() == DATE_RANGE_PATTERN_LENGTH) {
            handleDateRange(conditions, token, fieldName);
        } else {
            handleSingleDate(conditions, token, fieldName);
        }
    }

    private void handleDateRange(final ConditionalSupplier conditions, final Token token, final String fieldName) {
        final LocalDateTime ldt1 = LocalDateTime.parse(token
            .getDateSqlData()
            .substring(0, 19), DateTimeFormatter.ofPattern(DATE_FORMAT));
        final LocalDateTime ldt2 = LocalDateTime.parse(token
            .getDateSqlData()
            .substring(20), DateTimeFormatter.ofPattern(DATE_FORMAT));
        addToConditions(token, DSL.field(fieldName), DSL.val(Timestamp.valueOf(ldt1)), DSL.val(Timestamp.valueOf(ldt2)),
            conditions);
    }

    private void handleSingleDate(final ConditionalSupplier conditions, final Token token, final String fieldName) {
        final LocalDateTime ldt = LocalDateTime.parse(token.getDateSqlData(), DateTimeFormatter.ofPattern(DATE_FORMAT));
        addToConditions(token, DSL.field(fieldName), DSL.val(Timestamp.valueOf(ldt)), DSL.val(Timestamp.valueOf(ldt)),
            conditions);
    }

    private void addToConditions(final Token token, final Field<Object> field, final Field<Timestamp> value1,
        final Field<Timestamp> value2, final ConditionalSupplier conditions) {
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
