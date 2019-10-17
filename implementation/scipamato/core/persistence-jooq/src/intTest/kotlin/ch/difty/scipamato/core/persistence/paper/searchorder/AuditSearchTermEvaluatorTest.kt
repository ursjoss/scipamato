package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.AuditSearchTerm
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.Token
import ch.difty.scipamato.core.entity.search.AuditSearchTerm.TokenType
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName")
internal open class AuditSearchTermEvaluatorTest {

    private val e = AuditSearchTermEvaluator()

    private val tokens = ArrayList<Token>()

    private val stMock = mock<AuditSearchTerm>()

    private fun expectToken(type: TokenType, term: String, fieldName: String?) {
        fieldName?.let { whenever(stMock.fieldName).thenReturn(fieldName) }
        tokens.add(Token(type, term))
        whenever(stMock.tokens).thenReturn(tokens)
    }

    @Test
    fun buildingConditionForGreaterOrEqual_applies() {
        expectToken(TokenType.GREATEROREQUAL, "2017-01-12 00:00:00", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created >= timestamp '2017-01-12 00:00:00.0'")
    }

    @Test
    fun buildingConditionForGreaterThan_applies() {
        expectToken(TokenType.GREATERTHAN, "2017-01-12 00:00:00", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created > timestamp '2017-01-12 00:00:00.0'")
    }

    @Test
    fun buildingConditionForExact_appliesRegex() {
        expectToken(TokenType.EXACT, "2017-01-12 00:00:00", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created = timestamp '2017-01-12 00:00:00.0'")
    }

    @Test
    fun buildingConditionForLessThan_applies() {
        expectToken(TokenType.LESSTHAN, "2017-01-12 00:00:00", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created < timestamp '2017-01-12 00:00:00.0'")
    }

    @Test
    fun buildingConditionForLessThanOrEqual_applies() {
        expectToken(TokenType.LESSOREQUAL, "2017-01-12 00:00:00", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo("paper.created <= timestamp '2017-01-12 00:00:00.0'")
    }

    @Test
    fun buildingConditionForDateRange_applies() {
        expectToken(TokenType.RANGEQUOTED, "2017-01-11 10:00:00-2017-01-12 15:14:13", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualTo(
            "paper.created between timestamp '2017-01-11 10:00:00.0' and timestamp '2017-01-12 15:14:13.0'"
        )
    }

    @Test
    fun buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ", null)
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForWhitespace_appliesTrueCondition2() {
        expectToken(TokenType.WHITESPACE, "   ", null)
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo", "paper.created_by")
        assertThat(e.evaluate(stMock).toString()).isEqualToIgnoringCase(
            """"public"."paper"."id" in (
                   |  select "public"."paper"."id"
                   |  from "public"."paper"
                   |    join "public"."scipamato_user"
                   |      on paper.created_by = "public"."scipamato_user"."id"
                   |  where lower("public"."scipamato_user"."user_name") like '%foo%'
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForWord_appliesContains2() {
        expectToken(TokenType.WORD, "foo", "paper.last_modified_by")
        assertThat(e.evaluate(stMock).toString()).isEqualToIgnoringCase(
            """"public"."paper"."id" in (
                  |  select "public"."paper"."id"
                  |  from "public"."paper"
                  |    join "public"."scipamato_user"
                  |      on paper.last_modified_by = "public"."scipamato_user"."id"
                  |  where lower("public"."scipamato_user"."user_name") like '%foo%'
                  |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForGreaterOrEquals() {
        expectToken(TokenType.GREATEROREQUAL, "2019-01-05", "paper.created")
        assertThat(e.evaluate(stMock).toString()).isEqualToIgnoringCase(
            "paper.created >= timestamp '2019-01-05 00:00:00.0'"
        )
    }

    @Test
    fun buildingConditionForGreaterOrEquals2() {
        expectToken(TokenType.GREATEROREQUAL, "2019-01-05", "paper.last_modified")
        assertThat(e.evaluate(stMock).toString()).isEqualToIgnoringCase(
            "paper.last_modified >= timestamp '2019-01-05 00:00:00.0'"
        )
    }

    @Test
    fun buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo", null)
        assertThat(e.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForWrongField_withExactMatch_throws() {
        expectToken(TokenType.EXACT, "foo", "bar")
        validateDegenerateField(
            "Field bar is not one of the expected date fields [paper.created, " +
                "paper.last_modified] entitled to use MatchType.EQUALS"
        )
    }

    @Test
    fun buildingConditionForWrongField_withContainedMatch_throws() {
        expectToken(TokenType.WORD, "foo", "baz")
        validateDegenerateField(
            "Field baz is not one of the expected user fields [paper.created_by, " +
                "paper.last_modified_by] entitled to use MatchType.CONTAINS"
        )
    }

    @Suppress("TooGenericExceptionCaught")
    private fun validateDegenerateField(msg: String) {
        try {
            e.evaluate(stMock)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(msg)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun buildingConditionForWord_withNonUserField_throws() {
        expectToken(TokenType.WORD, "foo", "firstAuthor")
        try {
            e.evaluate(stMock)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage(
                    "Field firstAuthor is not one of the expected user fields [paper.created_by, " +
                        "paper.last_modified_by] entitled to use MatchType.CONTAINS"
                )
        }
    }
}
