package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.AuditSearchTerm.MatchType
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.TokenType
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

private const val CREATED = "CREATED"
private const val CREATED_BY = "CREATED_BY"

@Suppress("LongParameterList")
internal class AuditSearchTermTest {

    private fun assertSingleToken(
        st: AuditSearchTerm,
        fieldName: String,
        tt: TokenType,
        userRawData: String?,
        userData: String?,
        dateRawData: String?,
        dateData: String?
    ) {
        st.fieldName shouldBeEqualTo fieldName
        st.tokens shouldHaveSize 1
        assertToken(st, 0, tt, userRawData, userData, dateRawData, dateData)
    }

    @Suppress("SameParameterValue")
    private fun assertToken(
        st: AuditSearchTerm,
        idx: Int,
        tt: TokenType,
        userRawData: String?,
        userData: String?,
        dateRawData: String?,
        dateData: String?
    ) {
        with(st.tokens[idx]) {
            if (userRawData != null) {
                userRawData shouldBeEqualTo userRawData
                userSqlData shouldBeEqualTo userData
            } else {
                this.userRawData.shouldBeNull()
                userSqlData.shouldBeNull()
            }
            if (dateRawData != null) {
                dateRawData shouldBeEqualTo dateRawData
                dateSqlData shouldBeEqualTo dateData
            } else {
                this.dateRawData.shouldBeNull()
                dateSqlData.shouldBeNull()
            }
            type shouldBeEqualTo tt
        }
    }

    @Test
    fun lexingUserSpecs_findsUserOnly() {
        val fieldName = CREATED_BY
        val st = AuditSearchTerm(fieldName, "mkj")
        assertSingleToken(st, fieldName, TokenType.WORD, "mkj", "mkj", null, null)
    }

    @Test
    fun lexingUserSpecsForNonUserField_findsNothing() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "mkj")
        st.fieldName shouldBeEqualTo fieldName
        st.tokens.shouldBeEmpty()
    }

    @Test
    fun lexingMinimumDate_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, ">=2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.GREATEROREQUAL,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMinimumDateWithoutTime_usesTimestampAtStartOfDay() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, ">=2017-12-01")
        assertSingleToken(
            st, fieldName, TokenType.GREATEROREQUAL,
            null, null, "2017-12-01", "2017-12-01 00:00:00"
        )
    }

    @Test
    fun lexingMinimumDateQuoted_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, ">=\"2017-12-01 23:15:13\"")
        assertSingleToken(
            st, fieldName, TokenType.GREATEROREQUALQUOTED,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMinimumDateExcluded_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, ">2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.GREATERTHAN,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMinimumDateExcludedWithoutDate_usesTimestampAtEndOfDay() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, ">2017-12-01")
        assertSingleToken(
            st, fieldName, TokenType.GREATERTHAN,
            null, null, "2017-12-01", "2017-12-01 23:59:59"
        )
    }

    @Test
    fun lexingMinimumDateExcludedQuoted_findsDate() {
        val fieldName = "LAST_MODIFIED"
        val st = AuditSearchTerm(fieldName, ">\"2017-12-01 23:15:13\"")
        assertSingleToken(
            st, fieldName, TokenType.GREATERTHANQUOTED,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMaximumDate_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<=2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.LESSOREQUAL,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMaximumDateWithoutTime_usesTimestampAtEndOfDay() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<=2017-12-01")
        assertSingleToken(
            st, fieldName, TokenType.LESSOREQUAL,
            null, null, "2017-12-01", "2017-12-01 23:59:59"
        )
    }

    @Test
    fun lexingMaximumDateQuoted_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<=\"2017-12-01 23:15:13\"")
        assertSingleToken(st, fieldName, TokenType.LESSOREQUALQUOTED,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMaximumDateExcludedQuoted_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<\"2017-12-01 23:15:13\"")
        assertSingleToken(st, fieldName, TokenType.LESSTHANQUOTED,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingMaximumDateExcludedQuotedWithoutTime_usesTimestampAtStartOfDay() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<\"2017-12-01\"")
        assertSingleToken(st, fieldName, TokenType.LESSTHANQUOTED,
            null, null, "2017-12-01", "2017-12-01 00:00:00"
        )
    }

    @Test
    fun lexingMaximumDateExcluded_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "<2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.LESSTHAN,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingExactDate_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.EXACT,
            null, null, "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingDateRangeQuoted_withEquals_findsBothDates() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=\"2017-12-01 10:15:13\"-\"2017-12-02 23:12:11\"")
        assertSingleToken(
            st, fieldName, TokenType.RANGEQUOTED,
            null, null,
            "2017-12-01 10:15:13-2017-12-02 23:12:11", "2017-12-01 10:15:13-2017-12-02 23:12:11"
        )
    }

    @Test
    fun lexingDateRangeUnquoted_withEquals_findsBothDates() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=2017-12-01 10:15:13-2017-12-02 23:12:11")
        assertSingleToken(
            st, fieldName, TokenType.RANGE, null, null,
            "2017-12-01 10:15:13-2017-12-02 23:12:11", "2017-12-01 10:15:13-2017-12-02 23:12:11"
        )
    }

    @Test
    fun lexingDateRangeUnquoted_withoutEquals_findsBothDates() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "2017-12-01 10:15:13-2017-12-02 23:12:11")
        assertSingleToken(
            st, fieldName, TokenType.RANGE, null, null,
            "2017-12-01 10:15:13-2017-12-02 23:12:11", "2017-12-01 10:15:13-2017-12-02 23:12:11"
        )
    }

    @Test
    fun lexingDateRange_findsBothDates() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "\"2017-12-01 10:15:13\"-\"2017-12-02 23:12:11\"")
        assertSingleToken(
            st, fieldName, TokenType.RANGEQUOTED,
            null, null, "2017-12-01 10:15:13-2017-12-02 23:12:11",
            "2017-12-01 10:15:13-2017-12-02 23:12:11"
        )
    }

    @Test
    fun lexingDateRangeQuoted_withDatePartOnly_findsBothDatesExtended() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=\"2017-12-01\"-\"2017-12-02\"")
        assertSingleToken(
            st, fieldName, TokenType.RANGEQUOTED, null, null,
            "2017-12-01 00:00:00-2017-12-02 23:59:59", "2017-12-01 00:00:00-2017-12-02 23:59:59"
        )
    }

    @Test
    fun lexingDateRangeUnquoted_withDatePartOnly_findsBothDatesExtended() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=2017-12-01-2017-12-02")
        assertSingleToken(
            st, fieldName, TokenType.RANGE, null, null,
            "2017-12-01 00:00:00-2017-12-02 23:59:59", "2017-12-01 00:00:00-2017-12-02 23:59:59"
        )
    }

    @Test
    fun lexingDateRangeUnquoted_withDatePartOnly2_findsBothDatesExtended() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "2017-12-01-2017-12-02")
        assertSingleToken(
            st, fieldName, TokenType.RANGE, null, null,
            "2017-12-01 00:00:00-2017-12-02 23:59:59", "2017-12-01 00:00:00-2017-12-02 23:59:59"
        )
    }

    @Test
    fun lexingDateRange_withMixedDateParts_findsBothDatesExtended() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "\"2017-12-01 12:13:14\"-\"2017-12-02\"")
        assertSingleToken(
            st, fieldName, TokenType.RANGEQUOTED, null, null,
            "2017-12-01 12:13:14-2017-12-02 23:59:59", "2017-12-01 12:13:14-2017-12-02 23:59:59"
        )
    }

    @Test
    fun lexingDateRange_withMixedDateParts2_findsBothDatesExtended() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "\"2017-12-01\"-\"2017-12-02 14:15:16\"")
        assertSingleToken(st, fieldName, TokenType.RANGEQUOTED, null, null, "2017-12-01 00:00:00-2017-12-02 14:15:16",
            "2017-12-01 00:00:00-2017-12-02 14:15:16")
    }

    /**
     * This might turn out questionable and might have to be rewritten to include
     * the entire day. Let's see
     */
    @Test
    fun lexingExactDateWithoutTime_usesTimestampAtStartOfDay() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "2017-12-01")
        assertSingleToken(st, fieldName, TokenType.EXACT, null, null, "2017-12-01", "2017-12-01 00:00:00")
    }

    @Test
    fun lexingExactDateQuoted_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "\"2017-12-01 23:15:13\"")
        assertSingleToken(
            st, fieldName, TokenType.EXACTQUOTED, null, null,
            "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingExactDateWithEquals_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=2017-12-01 23:15:13")
        assertSingleToken(
            st, fieldName, TokenType.EXACT, null, null,
            "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingExactDateQuotedWithEquals_findsDate() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "=\"2017-12-01 23:15:13\"")
        assertSingleToken(
            st, fieldName, TokenType.EXACTQUOTED, null, null,
            "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Test
    fun lexingImproperDate_findsNothing() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "\"2017-12- 01 23:15:13\"")
        st.fieldName shouldBeEqualTo fieldName
        st.tokens.shouldBeEmpty()
    }

    @Test
    fun lexingUserAndDate_forUserField_findsUserTokenOnly() {
        val fieldName = CREATED_BY
        val st = AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"")
        assertSingleToken(st, fieldName, TokenType.WORD, "user", "user", null, null)
    }

    @Test
    fun lexingUserAndDate_forDateField_findsDateTokenOnly() {
        val fieldName = CREATED
        val st = AuditSearchTerm(fieldName, "user =\"2017-12-01 23:15:13\"")
        assertSingleToken(
            st, fieldName, TokenType.EXACTQUOTED, null, null,
            "2017-12-01 23:15:13", "2017-12-01 23:15:13"
        )
    }

    @Suppress("SpellCheckingInspection")
    @Test
    fun tokenToString_forDateField() {
        val st = AuditSearchTerm(CREATED, "user =\"2017-12-01 23:15:13\"")
        st.tokens shouldHaveSize 1
        st.tokens[0].toString() shouldBeEqualTo "(DATE EXACTQUOTED 2017-12-01 23:15:13)"
    }

    @Test
    fun tokenToString_forUserField() {
        val st = AuditSearchTerm(CREATED_BY, "foo =\"2017-12-01 23:15:13\"")
        st.tokens shouldHaveSize 1
        st.tokens[0].toString() shouldBeEqualTo "(USER WORD foo)"
    }

    @Test
    fun byMatchType_withValidMatchTypeNONE() {
        TokenType.byMatchType(MatchType.NONE) shouldContainAll listOf(TokenType.WHITESPACE, TokenType.RAW)
    }

    @Test
    fun byMatchType_withValidMatchTypeRANGE() {
        TokenType.byMatchType(MatchType.RANGE) shouldContainAll listOf(TokenType.RANGEQUOTED, TokenType.RANGE)
    }

    @Test
    fun byMatchType_withValidMatchTypeGREATER_OR_EQUAL() {
        TokenType.byMatchType(MatchType.GREATER_OR_EQUAL) shouldContainSame
            listOf(TokenType.GREATEROREQUALQUOTED, TokenType.GREATEROREQUAL)
    }

    @Test
    fun byMatchType_withValidMatchTypeGREATER_THAN() {
        TokenType.byMatchType(MatchType.GREATER_THAN) shouldContainSame
            listOf(TokenType.GREATERTHANQUOTED, TokenType.GREATERTHAN)
    }

    @Test
    fun byMatchType_withValidMatchTypeLESS_OR_EQUAL() {
        TokenType.byMatchType(MatchType.LESS_OR_EQUAL) shouldContainSame
            listOf(TokenType.LESSOREQUALQUOTED, TokenType.LESSOREQUAL)
    }

    @Test
    fun byMatchType_withValidMatchTypeLESS_THAN() {
        TokenType.byMatchType(MatchType.LESS_THAN) shouldContainSame
            listOf(TokenType.LESSTHANQUOTED, TokenType.LESSTHAN)
    }

    @Test
    fun byMatchType_withValidMatchTypeEQUALS() {
        TokenType.byMatchType(MatchType.EQUALS) shouldContainAll listOf(TokenType.EXACTQUOTED, TokenType.EXACT)
    }

    @Test
    fun byMatchType_withValidMatchTypeCONTAINS() {
        TokenType.byMatchType(MatchType.CONTAINS) shouldContainAll listOf(TokenType.WORD)
    }
}
