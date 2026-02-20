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
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@Suppress("SpellCheckingInspection")
internal class StringSearchTermEvaluatorIntegrationTest : SearchTermEvaluatorIntegrationTest<StringSearchTerm>() {

    override val searchTermType = SearchTermType.STRING.id

    private val evaluator = StringSearchTermEvaluator()

    override fun makeSearchTerm(rawSearchTerm: String) =
        SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm) as StringSearchTerm

    @TestFactory
    @Suppress("LongMethod")
    fun stringTests(): List<DynamicTest> = mapOf(
        "foo" to StringExp(
            tokenString = "(WORD foo)",
            condition = """cast(
                           |  fn
                           |  as varchar
                           |) ilike (('%' || replace(
                           |  replace(
                           |    replace('foo', '!', '!!'),
                           |    '%',
                           |    '!%'
                           |  ),
                           |  '_',
                           |  '!_'
                           |)) || '%') escape '!'""".trimMargin(),
            type = CONTAINS),

        "-foo" to StringExp(
            tokenString = "(NOTWORD foo)",
            condition = """not (cast(
                          |  coalesce(
                          |    fn,
                          |    ''
                          |  )
                          |  as varchar
                          |) ilike (('%' || replace(
                          |  replace(
                          |    replace('foo', '!', '!!'),
                          |    '%',
                          |    '!%'
                          |  ),
                          |  '_',
                          |  '!_'
                          |)) || '%') escape '!')""".trimMargin(),
            type = CONTAINS),
        """"foo"""" to StringExp(
            tokenString = "(QUOTED foo)",
            condition = """lower(cast(
                |  fn
                |  as varchar
                |)) = lower('foo')""".trimMargin(),
            type = EQUALS
        ),
        """-"foo"""" to StringExp(
            tokenString = "(NOTQUOTED foo)",
            condition = """lower(cast(
                |  fn
                |  as varchar
                |)) <> lower('foo')""".trimMargin(),
            type = EQUALS
        ),
        """="foo"""" to StringExp(
            tokenString = "(QUOTED foo)",
            condition = """lower(cast(
                |  fn
                |  as varchar
                |)) = lower('foo')""".trimMargin(),
            type = EQUALS
        ),

        """*foo""" to StringExp(
            tokenString = "(OPENLEFT %foo)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike '%foo'""".trimMargin(),
            type = LIKE
        ),

        """-*foo""" to StringExp(
            tokenString = "(NOTOPENLEFT %foo)",
            condition = """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike '%foo'""".trimMargin(),
            type = LIKE),
        """"*foo""""" to StringExp(
            tokenString = "(OPENLEFTQUOTED %foo)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike '%foo'""".trimMargin(),
            type = LIKE
        ),

        """-"*foo"""" to StringExp(
            tokenString = "(NOTOPENLEFTQUOTED %foo)",
            condition = """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike '%foo'""".trimMargin(),
            type = LIKE),

        """*foo*""" to StringExp(
            tokenString = "(OPENLEFTRIGHT %foo%)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike '%foo%'""".trimMargin(),
            type = LIKE),

        """-*foo*""" to StringExp(
            tokenString = "(NOTOPENLEFTRIGHT %foo%)",
            condition = """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike '%foo%'""".trimMargin(),
            type = LIKE),

        """"*foo*"""" to StringExp(
            tokenString = "(OPENLEFTRIGHTQUOTED %foo%)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike '%foo%'""".trimMargin(),
            type = LIKE),

        """-"*foo*"""" to StringExp(
            tokenString = "(NOTOPENLEFTRIGHTQUOTED %foo%)",
            condition = """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike '%foo%'""".trimMargin(),
            type = LIKE),

        """foo*""" to StringExp(
            tokenString = "(OPENRIGHT foo%)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike 'foo%'""".trimMargin(),
            type = LIKE
        ),

        """-foo*"""" to StringExp(
            tokenString = "(NOTOPENRIGHT foo%)",
            condition = """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike 'foo%'""".trimMargin(),
            type = LIKE),
        """"foo*"""" to StringExp(
            tokenString = "(OPENRIGHTQUOTED foo%)",
            condition = """cast(
                |  fn
                |  as varchar
                |) ilike 'foo%'""".trimMargin(),
            type = LIKE
        ),

        """-"foo*"""" to StringExp("(NOTOPENRIGHTQUOTED foo%)",
            """cast(
                |  coalesce(
                |    fn,
                |    ''
                |  )
                |  as varchar
                |) not ilike 'foo%'""".trimMargin(),
            LIKE
        ),

        """>""""" to StringExp("""(SOME >"")""",
            """(
                      |  fn is not null
                      |  and char_length(cast(
                      |    fn
                      |    as varchar
                      |  )) > 0
                      |)""".trimMargin(),
            LENGTH),

        """=""""" to StringExp("""(EMPTY ="")""",
            """(
                      |  fn is null
                      |  or char_length(cast(
                      |    fn
                      |    as varchar
                      |  )) = 0
                      |)""".trimMargin(),
            LENGTH
        ),
        """-""""" to StringExp("""(RAW -"")""", "true", NONE),

        """s/foo/""" to StringExp("(REGEX foo)", "(coalesce(\n  fn,\n  ''\n) like_regex 'foo')", REGEX),
        """-s/foo/""" to StringExp("(NOTREGEX foo)", "not ((coalesce(\n  fn,\n  ''\n) like_regex 'foo'))", REGEX),

        """""""" to StringExp("""(RAW "")""", "true", NONE),
    ).map { (rawSearchTerm, exp) ->
        DynamicTest.dynamicTest("$rawSearchTerm -> $exp") {
            val st = makeSearchTerm(rawSearchTerm)
            st.tokens.joinToString("") shouldBeEqualTo exp.tokenString
            st.tokens.first().type.matchType shouldBeEqualTo exp.type
            evaluator.evaluate(st).toString() shouldBeEqualTo exp.condition
        }
    }
}

private data class StringExp(
    val tokenString: String,
    val condition: String,
    val type: MatchType,
)
