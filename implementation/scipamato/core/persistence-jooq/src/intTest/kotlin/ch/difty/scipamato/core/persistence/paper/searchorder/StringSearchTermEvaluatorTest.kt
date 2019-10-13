package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import ch.difty.scipamato.core.entity.search.StringSearchTerm
import ch.difty.scipamato.core.entity.search.StringSearchTerm.Token
import ch.difty.scipamato.core.entity.search.StringSearchTerm.TokenType
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test

@Suppress("LargeClass", "TooManyFunctions", "FunctionName", "LongMethod", "DuplicatedCode", "TooGenericExceptionCaught")
internal class StringSearchTermEvaluatorTest {

    private val evaluator = StringSearchTermEvaluator()

    private val tokens = mutableListOf<Token>()

    private val stMock = mock<StringSearchTerm>()

    //region:normalField
    private fun expectToken(type: TokenType, term: String) {
        whenever(stMock.fieldName).thenReturn("field_x")
        tokens.add(Token(type, term))
        whenever(stMock.tokens).thenReturn(tokens)
    }

    @Test
    fun buildingConditionForNotRegex_appliesNotRegex() {
        expectToken(TokenType.NOTREGEX, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """not(coalesce(
                   |  field_x, 
                   |  ''
                   |) like_regex 'foo')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForRegex_appliesRegex() {
        expectToken(TokenType.REGEX, "foo")
        assertThat(evaluator
            .evaluate(stMock)
            .toString()).isEqualTo(
            """coalesce(
                   |  field_x, 
                   |  ''
                   |) like_regex 'foo'""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForWhitespace_appliesTrueCondition() {
        expectToken(TokenType.WHITESPACE, "   ")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForSome_appliesNotEmpty() {
        expectToken(TokenType.SOME, "whatever")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  field_x is not null
               |  and char_length(cast(field_x as varchar)) > 0
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForEmpty_appliesEmpty() {
        expectToken(TokenType.EMPTY, "whatever")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  field_x is null
               |  or char_length(cast(field_x as varchar)) = 0
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('%foo%')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftRightQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('%foo%')"
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFTRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('%foo%')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftRight_appliesLike() {
        expectToken(TokenType.OPENLEFTRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('%foo%')"
        )
    }

    @Test
    fun buildingConditionForNotOpenRightQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENRIGHTQUOTED, "foo")
        assertThat(evaluator
            .evaluate(stMock)
            .toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('foo%')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenRightQuoted_appliesLike() {
        expectToken(TokenType.OPENRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('foo%')"
        )
    }

    @Test
    fun buildingConditionForNotOpenRight_appliesNotLike() {
        expectToken(TokenType.NOTOPENRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('foo%')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenRight_appliesLike() {
        expectToken(TokenType.OPENRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('foo%')"
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.NOTOPENLEFTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('%foo')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftQuoted_appliesLike() {
        expectToken(TokenType.OPENLEFTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('%foo')"
        )
    }

    @Test
    fun buildingConditionForNotOpenLeft_appliesNotLike() {
        expectToken(TokenType.NOTOPENLEFT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(coalesce(
                   |  field_x, 
                   |  ''
                   |) as varchar)) not like lower('%foo')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeft_appliesLike() {
        expectToken(TokenType.OPENLEFT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) like lower('%foo')"
        )
    }

    @Test
    fun buildingConditionForNotQuoted_appliesUnequal() {
        expectToken(TokenType.NOTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) <> lower('foo')"
        )
    }

    @Test
    fun buildingConditionForQuoted_appliesEqual() {
        expectToken(TokenType.QUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            "lower(cast(field_x as varchar)) = lower('foo')"
        )
    }

    @Test
    fun buildingConditionForNotWord_appliesNotContains() {
        expectToken(TokenType.NOTWORD, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """not(lower(cast(coalesce(
               |  field_x, 
               |  ''
               |) as varchar)) like lower(('%' || replace(
               |  replace(
               |    replace(
               |      'foo', 
               |      '!', 
               |      '!!'
               |    ), 
               |    '%', 
               |    '!%'
               |  ), 
               |  '_', 
               |  '!_'
               |) || '%')) escape '!')""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForWord_appliesContains() {
        expectToken(TokenType.WORD, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """lower(cast(field_x as varchar)) like lower(('%' || replace(
               |  replace(
               |    replace(
               |      'foo', 
               |      '!', 
               |      '!!'
               |    ), 
               |    '%', 
               |    '!%'
               |  ), 
               |  '_', 
               |  '!_'
               |) || '%')) escape '!'""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForRaw_appliesDummyTrue() {
        expectToken(TokenType.RAW, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForUnsupported_throws() {
        expectToken(TokenType.UNSUPPORTED, "foo")
        try {
            evaluator.evaluate(stMock)
            fail<Any>("should have thrown exception")
        } catch (ex: Error) {
            assertThat(ex)
                .isInstanceOf(AssertionError::class.java)
                .hasMessage("Evaluation of type UNSUPPORTED is not supported...")
        }
    }
    //endregion

    //region:methodsField
    private fun expectMethodToken(type: TokenType, term: String) {
        whenever(stMock.fieldName).thenReturn("methods")
        tokens.add(Token(type, term))
        whenever(stMock.tokens).thenReturn(tokens)
    }

    @Test
    fun buildingConditionForNotRegex_withMethodsField_appliesNotRegexToAllMethodsFields() {
        expectMethodToken(TokenType.NOTREGEX, "foo")
        assertThat(evaluator
            .evaluate(stMock)
            .toString()).isEqualTo(
            """(
               |  not(lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |  and not(lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) like_regex 'foo')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForRegex_withMethodsField_appliesRegexToAllMethodsFields() {
        expectMethodToken(TokenType.REGEX, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |  or lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) like_regex 'foo'
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForWhitespace_withMethodField_appliesTrueCondition() {
        expectMethodToken(TokenType.WHITESPACE, "   ")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Test
    fun buildingConditionForSome_withMethodField_appliesNotEmpty() {
        expectMethodToken(TokenType.SOME, "whatever")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  (
                   |    methods is not null
                   |    and char_length(cast(methods as varchar)) > 0
                   |  )
                   |  or (
                   |    method_study_design is not null
                   |    and char_length(cast(method_study_design as varchar)) > 0
                   |  )
                   |  or (
                   |    population_place is not null
                   |    and char_length(cast(population_place as varchar)) > 0
                   |  )
                   |  or (
                   |    method_outcome is not null
                   |    and char_length(cast(method_outcome as varchar)) > 0
                   |  )
                   |  or (
                   |    exposure_pollutant is not null
                   |    and char_length(cast(exposure_pollutant as varchar)) > 0
                   |  )
                   |  or (
                   |    exposure_assessment is not null
                   |    and char_length(cast(exposure_assessment as varchar)) > 0
                   |  )
                   |  or (
                   |    method_statistics is not null
                   |    and char_length(cast(method_statistics as varchar)) > 0
                   |  )
                   |  or (
                   |    method_confounders is not null
                   |    and char_length(cast(method_confounders as varchar)) > 0
                   |  )
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForEmpty_withMethodField_appliesEmptyToAllMethodsFields() {
        expectMethodToken(TokenType.EMPTY, "whatever")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  (
               |    methods is null
               |    or char_length(cast(methods as varchar)) = 0
               |  )
               |  and (
               |    method_study_design is null
               |    or char_length(cast(method_study_design as varchar)) = 0
               |  )
               |  and (
               |    population_place is null
               |    or char_length(cast(population_place as varchar)) = 0
               |  )
               |  and (
               |    method_outcome is null
               |    or char_length(cast(method_outcome as varchar)) = 0
               |  )
               |  and (
               |    exposure_pollutant is null
               |    or char_length(cast(exposure_pollutant as varchar)) = 0
               |  )
               |  and (
               |    exposure_assessment is null
               |    or char_length(cast(exposure_assessment as varchar)) = 0
               |  )
               |  and (
               |    method_statistics is null
               |    or char_length(cast(method_statistics as varchar)) = 0
               |  )
               |  and (
               |    method_confounders is null
               |    or char_length(cast(method_confounders as varchar)) = 0
               |  )
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |  and lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) not like lower('%foo%')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) like lower('%foo%')
                   |  or lower(cast(method_study_design as varchar)) like lower('%foo%')
                   |  or lower(cast(population_place as varchar)) like lower('%foo%')
                   |  or lower(cast(method_outcome as varchar)) like lower('%foo%')
                   |  or lower(cast(exposure_pollutant as varchar)) like lower('%foo%')
                   |  or lower(cast(exposure_assessment as varchar)) like lower('%foo%')
                   |  or lower(cast(method_statistics as varchar)) like lower('%foo%')
                   |  or lower(cast(method_confounders as varchar)) like lower('%foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftRight_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(coalesce(
                   |    methods, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    method_study_design, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    population_place, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    method_outcome, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_pollutant, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_assessment, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    method_statistics, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |  and lower(cast(coalesce(
                   |    method_confounders, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftRight_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) like lower('%foo%')
                   |  or lower(cast(method_study_design as varchar)) like lower('%foo%')
                   |  or lower(cast(population_place as varchar)) like lower('%foo%')
                   |  or lower(cast(method_outcome as varchar)) like lower('%foo%')
                   |  or lower(cast(exposure_pollutant as varchar)) like lower('%foo%')
                   |  or lower(cast(exposure_assessment as varchar)) like lower('%foo%')
                   |  or lower(cast(method_statistics as varchar)) like lower('%foo%')
                   |  or lower(cast(method_confounders as varchar)) like lower('%foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(coalesce(
                   |    methods, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_study_design, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    population_place, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_outcome, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_pollutant, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_assessment, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_statistics, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_confounders, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenRightQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENRIGHTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) like lower('foo%')
                   |  or lower(cast(method_study_design as varchar)) like lower('foo%')
                   |  or lower(cast(population_place as varchar)) like lower('foo%')
                   |  or lower(cast(method_outcome as varchar)) like lower('foo%')
                   |  or lower(cast(exposure_pollutant as varchar)) like lower('foo%')
                   |  or lower(cast(exposure_assessment as varchar)) like lower('foo%')
                   |  or lower(cast(method_statistics as varchar)) like lower('foo%')
                   |  or lower(cast(method_confounders as varchar)) like lower('foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenRight_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(coalesce(
                   |    methods, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_study_design, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    population_place, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_outcome, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_pollutant, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    exposure_assessment, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_statistics, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |  and lower(cast(coalesce(
                   |    method_confounders, 
                   |    ''
                   |  ) as varchar)) not like lower('foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenRight_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENRIGHT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) like lower('foo%')
                   |  or lower(cast(method_study_design as varchar)) like lower('foo%')
                   |  or lower(cast(population_place as varchar)) like lower('foo%')
                   |  or lower(cast(method_outcome as varchar)) like lower('foo%')
                   |  or lower(cast(exposure_pollutant as varchar)) like lower('foo%')
                   |  or lower(cast(exposure_assessment as varchar)) like lower('foo%')
                   |  or lower(cast(method_statistics as varchar)) like lower('foo%')
                   |  or lower(cast(method_confounders as varchar)) like lower('foo%')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenLeftQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(coalesce(
                   |    methods, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    method_study_design, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    population_place, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    method_outcome, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    exposure_pollutant, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    exposure_assessment, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    method_statistics, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |  and lower(cast(coalesce(
                   |    method_confounders, 
                   |    ''
                   |  ) as varchar)) not like lower('%foo')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeftQuoted_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(methods as varchar)) like lower('%foo')
               |  or lower(cast(method_study_design as varchar)) like lower('%foo')
               |  or lower(cast(population_place as varchar)) like lower('%foo')
               |  or lower(cast(method_outcome as varchar)) like lower('%foo')
               |  or lower(cast(exposure_pollutant as varchar)) like lower('%foo')
               |  or lower(cast(exposure_assessment as varchar)) like lower('%foo')
               |  or lower(cast(method_statistics as varchar)) like lower('%foo')
               |  or lower(cast(method_confounders as varchar)) like lower('%foo')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotOpenLeft_withMethodField_appliesNotLikeToAllMethodFields() {
        expectMethodToken(TokenType.NOTOPENLEFT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |  and lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) not like lower('%foo')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForOpenLeft_withMethodField_appliesLikeToAllMethodFields() {
        expectMethodToken(TokenType.OPENLEFT, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(methods as varchar)) like lower('%foo')
               |  or lower(cast(method_study_design as varchar)) like lower('%foo')
               |  or lower(cast(population_place as varchar)) like lower('%foo')
               |  or lower(cast(method_outcome as varchar)) like lower('%foo')
               |  or lower(cast(exposure_pollutant as varchar)) like lower('%foo')
               |  or lower(cast(exposure_assessment as varchar)) like lower('%foo')
               |  or lower(cast(method_statistics as varchar)) like lower('%foo')
               |  or lower(cast(method_confounders as varchar)) like lower('%foo')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotQuoted_withMethodField_appliesUnequalToAllMethodFields() {
        expectMethodToken(TokenType.NOTQUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) <> lower('foo')
                   |  and lower(cast(method_study_design as varchar)) <> lower('foo')
                   |  and lower(cast(population_place as varchar)) <> lower('foo')
                   |  and lower(cast(method_outcome as varchar)) <> lower('foo')
                   |  and lower(cast(exposure_pollutant as varchar)) <> lower('foo')
                   |  and lower(cast(exposure_assessment as varchar)) <> lower('foo')
                   |  and lower(cast(method_statistics as varchar)) <> lower('foo')
                   |  and lower(cast(method_confounders as varchar)) <> lower('foo')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForQuoted_withMethodField_appliesEqualToAllMethodFields() {
        expectMethodToken(TokenType.QUOTED, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
                   |  lower(cast(methods as varchar)) = lower('foo')
                   |  or lower(cast(method_study_design as varchar)) = lower('foo')
                   |  or lower(cast(population_place as varchar)) = lower('foo')
                   |  or lower(cast(method_outcome as varchar)) = lower('foo')
                   |  or lower(cast(exposure_pollutant as varchar)) = lower('foo')
                   |  or lower(cast(exposure_assessment as varchar)) = lower('foo')
                   |  or lower(cast(method_statistics as varchar)) = lower('foo')
                   |  or lower(cast(method_confounders as varchar)) = lower('foo')
                   |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForNotWord_withMethodField_appliesNotContainsToAllMethodFields() {
        expectMethodToken(TokenType.NOTWORD, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  not(lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForWord_withMethodField_appliesContainsToAllMethodFields() {
        expectMethodToken(TokenType.WORD, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo(
            """(
               |  lower(cast(methods as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(method_study_design as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(population_place as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(method_outcome as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(exposure_pollutant as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(exposure_assessment as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(method_statistics as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |  or lower(cast(method_confounders as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'foo', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!'
               |)""".trimMargin()
        )
    }

    @Test
    fun buildingConditionForRaw_withMethodField_appliesDummyTrue() {
        expectMethodToken(TokenType.RAW, "foo")
        assertThat(evaluator.evaluate(stMock).toString()).isEqualTo("1 = 1")
    }

    @Suppress("TooGenericExceptionCaught")
    @Test
    fun buildingConditionForUnsupported_withMethodField_throws() {
        expectMethodToken(TokenType.UNSUPPORTED, "foo")
        try {
            evaluator.evaluate(stMock)
            fail<Any>("should have thrown exception")
        } catch (ex: Error) {
            assertThat(ex)
                .isInstanceOf(AssertionError::class.java)
                .hasMessage("Evaluation of type UNSUPPORTED is not supported...")
        }
    }

    @Test
    fun buildingConditionForCombinedSearchTerm_withMethodsField_() {
        val sst = SearchTerm.newSearchTerm(1L, SearchTermType.STRING.id, 1L,
            "methods", "foo -bar") as StringSearchTerm
        assertThat(evaluator.evaluate(sst).toString()).isEqualTo(
            """(
               |  (
               |    lower(cast(methods as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(method_study_design as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(population_place as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(method_outcome as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(exposure_pollutant as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(exposure_assessment as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(method_statistics as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |    or lower(cast(method_confounders as varchar)) like lower(('%' || replace(
               |      replace(
               |        replace(
               |          'foo', 
               |          '!', 
               |          '!!'
               |        ), 
               |        '%', 
               |        '!%'
               |      ), 
               |      '_', 
               |      '!_'
               |    ) || '%')) escape '!'
               |  )
               |  and not(lower(cast(coalesce(
               |    methods, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_study_design, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    population_place, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_outcome, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    exposure_pollutant, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    exposure_assessment, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_statistics, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |  and not(lower(cast(coalesce(
               |    method_confounders, 
               |    ''
               |  ) as varchar)) like lower(('%' || replace(
               |    replace(
               |      replace(
               |        'bar', 
               |        '!', 
               |        '!!'
               |      ), 
               |      '%', 
               |      '!%'
               |    ), 
               |    '_', 
               |    '!_'
               |  ) || '%')) escape '!')
               |)""".trimMargin()
        )
    }
    //endregion
}
