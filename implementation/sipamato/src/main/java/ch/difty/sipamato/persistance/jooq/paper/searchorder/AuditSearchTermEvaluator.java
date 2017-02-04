package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static ch.difty.sipamato.db.tables.Paper.PAPER;
import static ch.difty.sipamato.db.tables.User.USER;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.filter.AuditSearchTerm;
import ch.difty.sipamato.entity.filter.AuditSearchTerm.MatchType;
import ch.difty.sipamato.entity.filter.AuditSearchTerm.Token;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.persistance.jooq.ConditionalSupplier;

/**
 * {@link SearchTermEvaluator} implementation evaluating audit searchTerms.
 *
 * @author u.joss
 */
class AuditSearchTermEvaluator implements SearchTermEvaluator<AuditSearchTerm> {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** {@inheritDoc} */
    @Override
    public Condition evaluate(final AuditSearchTerm searchTerm) {
        AssertAs.notNull(searchTerm, "searchTerm");

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

    private void handleUserField(final AuditSearchTerm searchTerm, final ConditionalSupplier conditions, final Token token) {
        final String fieldName = searchTerm.getFieldName();
        assert (fieldName.equals(Paper.FLD_CREATED_BY) || fieldName.equals(Paper.FLD_LAST_MOD_BY));
        final Field<Object> field = DSL.field(fieldName);
        final String userName = "%" + token.getUserSqlData().toLowerCase() + "%";
        final SelectConditionStep<Record1<Long>> step = DSL.select(PAPER.ID).from(PAPER).innerJoin(USER).on(field.eq(USER.ID)).where(USER.USER_NAME.lower().like(userName));
        conditions.add(() -> PAPER.ID.in(step));
    }

    private void handleDateField(final AuditSearchTerm searchTerm, final ConditionalSupplier conditions, final Token token) {
        final String fieldName = searchTerm.getFieldName();
        assert (fieldName.equals(Paper.FLD_CREATED) || fieldName.equals(Paper.FLD_LAST_MOD));
        final Field<Object> field = DSL.field(fieldName);
        String searchString = token.getDateSqlData();
        LocalDateTime ldt = LocalDateTime.parse(searchString, DateTimeFormatter.ofPattern(DATE_FORMAT));
        final Field<Timestamp> value = DSL.val(Timestamp.valueOf(ldt));
        switch (token.getType().matchType) {
        case GREATER_THAN:
            conditions.add(() -> (field.greaterThan(value)));
            break;
        case GREATER_OR_EQUAL:
            conditions.add(() -> (field.greaterOrEqual(value)));
            break;
        case EQUALS:
            conditions.add(() -> (field.equal(value)));
            break;
        case LESS_OR_EQUAL:
            conditions.add(() -> (field.lessOrEqual(value)));
            break;
        case LESS_THAN:
            conditions.add(() -> (field.lessThan(value)));
            break;
        case CONTAINS:
            break;
        case NONE:
            break;
        default:
            throw new UnsupportedOperationException("Evaluation of type " + token.getType().matchType + " is not supported...");
        }
    }
}
