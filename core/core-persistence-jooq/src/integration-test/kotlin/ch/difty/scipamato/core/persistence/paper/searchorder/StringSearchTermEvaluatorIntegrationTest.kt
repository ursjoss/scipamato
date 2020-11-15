package ch.difty.scipamato.core.persistence.paper.searchorder

import ch.difty.scipamato.core.entity.search.SearchTerm
import ch.difty.scipamato.core.entity.search.SearchTermType
import ch.difty.scipamato.core.entity.search.StringSearchTerm
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.CONTAINS
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.EQUALS
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.LENGTH
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.LIKE
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.NONE
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.REGEX
import org.amshove.kluent.shouldBeEqualTo
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

    override fun makeSearchTerm(rawSearchTerm: String) =
        SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as StringSearchTerm

    @ParameterizedTest(name = "[{index}] {0} -> {1} [type {3}]")
    @MethodSource("stringParameters")
    fun stringTest(rawSearchTerm: String, tokenString: String, condition: String, type: MatchType) {
        val st = makeSearchTerm(rawSearchTerm)
        st.tokens.joinToString("") shouldBeEqualTo tokenString
        st.tokens.first().type.matchType shouldBeEqualTo type
        evaluator.evaluate(st).toString() shouldBeEqualTo condition
    }

    companion object {
        @Suppress("unused", "LongMethod")
        @JvmStatic
        fun stringParameters() = listOf(
            Arguments.of(
                "foo", "(WORD foo)",
                """fn ilike ('%' || replace(
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
                       |) || '%') escape '!'""".trimMargin(),
                CONTAINS
            ),
            Arguments.of(
                "-foo", "(NOTWORD foo)",
                """not (coalesce(
                      |  fn,
                      |  ''
                      |) ilike ('%' || replace(
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
                      |) || '%') escape '!')""".trimMargin(),
                CONTAINS
            ),
            Arguments.of(""""foo"""", "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS),
            Arguments.of("""-"foo"""", "(NOTQUOTED foo)", "lower(cast(fn as varchar)) <> lower('foo')", EQUALS),
            Arguments.of("""="foo"""", "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS),

            Arguments.of("""*foo""", "(OPENLEFT %foo)", "fn ilike '%foo'", LIKE),
            Arguments.of(
                """-*foo""", "(NOTOPENLEFT %foo)",
                """coalesce(
                |  fn,
                |  ''
                |) not ilike '%foo'""".trimMargin(),
                LIKE
            ),
            Arguments.of(""""*foo""""", "(OPENLEFTQUOTED %foo)", "fn ilike '%foo'", LIKE),
            Arguments.of(
                """-"*foo"""",
                "(NOTOPENLEFTQUOTED %foo)",
                """coalesce(
                    |  fn,
                    |  ''
                    |) not ilike '%foo'""".trimMargin(),
                LIKE
            ),

            Arguments.of(
                """*foo*""", "(OPENLEFTRIGHT %foo%)",
                "fn ilike '%foo%'",
                LIKE
            ),
            Arguments.of(
                """-*foo*""", "(NOTOPENLEFTRIGHT %foo%)",
                """coalesce(
                    |  fn,
                    |  ''
                    |) not ilike '%foo%'""".trimMargin(),
                LIKE
            ),
            Arguments.of(
                """"*foo*"""", "(OPENLEFTRIGHTQUOTED %foo%)",
                "fn ilike '%foo%'",
                LIKE
            ),
            Arguments.of(
                """-"*foo*"""", "(NOTOPENLEFTRIGHTQUOTED %foo%)",
                """coalesce(
                    |  fn,
                    |  ''
                    |) not ilike '%foo%'""".trimMargin(),
                LIKE
            ),

            Arguments.of("""foo*""", "(OPENRIGHT foo%)", "fn ilike 'foo%'", LIKE),
            Arguments.of(
                """-foo*"""", "(NOTOPENRIGHT foo%)",
                """coalesce(
                    |  fn,
                    |  ''
                    |) not ilike 'foo%'""".trimMargin(),
                LIKE
            ),
            Arguments.of(""""foo*"""", "(OPENRIGHTQUOTED foo%)", "fn ilike 'foo%'", LIKE),
            Arguments.of(
                """-"foo*"""", "(NOTOPENRIGHTQUOTED foo%)",
                """coalesce(
                    |  fn,
                    |  ''
                    |) not ilike 'foo%'""".trimMargin(),
                LIKE
            ),

            Arguments.of(
                """>""""", """(SOME >"")""",
                """(
                      |  fn is not null
                      |  and char_length(cast(fn as varchar)) > 0
                      |)""".trimMargin(),
                LENGTH
            ),
            Arguments.of(
                """=""""", """(EMPTY ="")""",
                """(
                      |  fn is null
                      |  or char_length(cast(fn as varchar)) = 0
                      |)""".trimMargin(),
                LENGTH
            ),
            Arguments.of("""-""""", """(RAW -"")""", "true", NONE),

            Arguments.of("""s/foo/""", "(REGEX foo)", "(coalesce(\n  fn,\n  ''\n) like_regex 'foo')", REGEX),
            Arguments.of("""-s/foo/""", "(NOTREGEX foo)", "not ((coalesce(\n  fn,\n  ''\n) like_regex 'foo'))", REGEX),

            Arguments.of("""""""", """(RAW "")""", "true", NONE)
        )
    }
}
