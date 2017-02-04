package ch.difty.sipamato.entity.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.sipamato.entity.filter.AuditSearchTerm.TokenType;

public class AuditSearchTermTest {

    private AuditSearchTerm st;

    private void assertSingleToken(String fieldName, TokenType tt, String userRawData, String userData, String dateRawData, String dateData) {
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).hasSize(1);
        assertToken(0, tt, userRawData, userData, dateRawData, dateData);
    }

    private void assertToken(int idx, TokenType tt, String userRawData, String userData, String dateRawData, String dateData) {
        if (userRawData != null) {
            assertThat(st.getTokens().get(idx).getUserRawData()).isEqualTo(userRawData);
            assertThat(st.getTokens().get(idx).getUserSqlData()).isEqualTo(userData);
        } else {
            assertThat(st.getTokens().get(idx).getUserRawData()).isNull();
            assertThat(st.getTokens().get(idx).getUserSqlData()).isNull();
        }
        if (dateRawData != null) {
            assertThat(st.getTokens().get(idx).getDateRawData()).isEqualTo(dateRawData);
            assertThat(st.getTokens().get(idx).getDateSqlData()).isEqualTo(dateData);
        } else {
            assertThat(st.getTokens().get(idx).getDateRawData()).isNull();
            assertThat(st.getTokens().get(idx).getDateSqlData()).isNull();
        }
        assertThat(st.getTokens().get(idx).getType()).isEqualTo(tt);
    }

    @Test
    public void lexingUserSpecs_findsUserOnly() {
        String fieldName = "CREATED_BY";
        st = new AuditSearchTerm(fieldName, "mkj");
        assertSingleToken(fieldName, TokenType.WORD, "mkj", "mkj", null, null);
    }

    @Test
    public void lexingUserSpecsForNonUserField_findsNothing() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "mkj");
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).isEmpty();
    }

    @Test
    public void lexingMinimumDate_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, ">=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.GREATEROREQUAL, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMinimumDateQuoted_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, ">=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.GREATEROREQUALQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMinimumDateExcluded_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, ">2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.GREATERTHAN, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMinimumDateExcludedQuoted_findsDate() {
        String fieldName = "LAST_MODIFIED";
        st = new AuditSearchTerm(fieldName, ">\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.GREATERTHANQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMaximumDate_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "<=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.LESSOREQUAL, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMaximumDateQuoted_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "<=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.LESSOREQUALQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMaximumDateExcludedQuoted_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "<\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.LESSTHANQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingMaximumDateExcluded_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "<2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.LESSTHAN, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingExactDate_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingExactDateQuoted_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingExactDateWithEquals_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "=2017-12-01 23:15:13");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingExactDateQuotedWithEquals_findsDate() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "=\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }

    @Test
    public void lexingExactDateWihtoutTime_findsDateWithTimeMidnight() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "=2017-12-01");
        assertSingleToken(fieldName, TokenType.EXACT, null, null, "2017-12-01 00:00:00", "2017-12-01 00:00:00");
    }

    @Test
    public void lexingImproperDate_findsNothing() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "\"2017-12- 01 23:15:13\"");
        assertThat(st.getFieldName()).isEqualTo(fieldName);
        assertThat(st.getTokens()).isEmpty();
    }

    @Test
    public void lexingUserAndDate_forUserField_findsUserTokenOnly() {
        String fieldName = "CREATED_BY";
        st = new AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.WORD, "user", "user", null, null);
    }

    @Test
    public void lexingUserAndDate_forDateField_findsDateTokenOnly() {
        String fieldName = "CREATED";
        st = new AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"");
        assertSingleToken(fieldName, TokenType.EXACTQUOTED, null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13");
    }
}
