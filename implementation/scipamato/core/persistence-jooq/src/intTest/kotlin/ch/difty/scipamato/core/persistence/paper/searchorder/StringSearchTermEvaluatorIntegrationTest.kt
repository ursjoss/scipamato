package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import ch.difty.scipamato.core.entity.search.StringSearchTerm
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@Suppress("SpellCheckingInspection")
internal class StringSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<StringSearchTerm>() {

    override val searchTermType = SearchTermType.STRING.id

    private val evaluator = StringSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) = SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as StringSearchTerm

    @ParameterizedTest(name = "[{index}] {0} -> {1} [type {3}]")
    @MethodSource("stringParameters")
    fun stringTest(rawSearchTerm: String, tokenString: String, condition: String, type: MatchType) {
        val st = makeSearchTerm(rawSearchTerm)
        assertThat(st.tokens.joinToString("")).isEqualTo(tokenString)
        assertThat(st.tokens.first().type.matchType).isEqualTo(type)
        assertThat(evaluator.evaluate(st).toString()).isEqualTo(condition)
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        fun stringParameters() = listOf(
                Arguments.of("foo", "(WORD foo)",
                        """lower(cast(fn as varchar)) like lower(('%' || replace(
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
                       |) || '%')) escape '!'""".trimMargin(), CONTAINS),
                Arguments.of("-foo", "(NOTWORD foo)",
                        """not(lower(cast(coalesce(
                      |  fn, 
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
                      |) || '%')) escape '!')""".trimMargin(), CONTAINS),
                Arguments.of(""""foo"""", "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS),
                Arguments.of("""-"foo"""", "(NOTQUOTED foo)", "lower(cast(fn as varchar)) <> lower('foo')", EQUALS),
                Arguments.of("""="foo"""", "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS),

                Arguments.of("""*foo""", "(OPENLEFT %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE),
                Arguments.of("""-*foo""", "(NOTOPENLEFT %foo)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('%foo')", LIKE),
                Arguments.of(""""*foo""""", "(OPENLEFTQUOTED %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE),
                Arguments.of("""-"*foo"""", "(NOTOPENLEFTQUOTED %foo)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('%foo')", LIKE),

                Arguments.of("""*foo*""", "(OPENLEFTRIGHT %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE),
                Arguments.of("""-*foo*""", "(NOTOPENLEFTRIGHT %foo%)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('%foo%')", LIKE),
                Arguments.of(""""*foo*"""", "(OPENLEFTRIGHTQUOTED %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE),
                Arguments.of("""-"*foo*"""", "(NOTOPENLEFTRIGHTQUOTED %foo%)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('%foo%')", LIKE),

                Arguments.of("""foo*""", "(OPENRIGHT foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE),
                Arguments.of("""-foo*"""", "(NOTOPENRIGHT foo%)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('foo%')", LIKE),
                Arguments.of(""""foo*"""", "(OPENRIGHTQUOTED foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE),
                Arguments.of("""-"foo*"""", "(NOTOPENRIGHTQUOTED foo%)", "lower(cast(coalesce(\n  fn, \n  ''\n) as varchar)) not like lower('foo%')", LIKE),

                Arguments.of(""">""""", """(SOME >"")""",
                        """(
                      |  fn is not null
                      |  and char_length(cast(fn as varchar)) > 0
                      |)""".trimMargin(), LENGTH),
                Arguments.of("""=""""", """(EMPTY ="")""",
                        """(
                      |  fn is null
                      |  or char_length(cast(fn as varchar)) = 0
                      |)""".trimMargin(), LENGTH),
                Arguments.of("""-""""", """(RAW -"")""", "1 = 1", NONE),

                Arguments.of("""s/foo/""", "(REGEX foo)", "coalesce(\n  fn, \n  ''\n) like_regex 'foo'", REGEX),
                Arguments.of("""-s/foo/""", "(NOTREGEX foo)", "not(coalesce(\n  fn, \n  ''\n) like_regex 'foo')", REGEX),

                Arguments.of("""""""", """(RAW "")""", "1 = 1", NONE)
        )
    }
}
