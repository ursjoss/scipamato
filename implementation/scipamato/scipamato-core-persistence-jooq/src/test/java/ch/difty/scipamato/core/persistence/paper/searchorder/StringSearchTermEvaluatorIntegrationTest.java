package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;
import ch.difty.scipamato.core.entity.search.StringSearchTerm;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.MatchType;
import ch.difty.scipamato.core.entity.search.StringSearchTerm.Token;

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@SuppressWarnings("SpellCheckingInspection")
class StringSearchTermEvaluatorIntegrationTest extends SearchTermEvaluatorIntegrationTest<StringSearchTerm> {

    private static Stream<Arguments> stringParameters() {
        return Stream.of(
            // @formatter:off
            Arguments.of( "foo", "(WORD foo)", concat(
                "lower(cast(fn as varchar)) like ('%' || replace(",
                "  replace(",
                "    replace(",
                "      lower('foo'), ",
                "      '!', ",
                "      '!!'",
                "    ), ",
                "    '%', ",
                "    '!%'",
                "  ), ",
                "  '_', ",
                "  '!_'",
                ") || '%') escape '!'"), CONTAINS ),
            Arguments.of( "-foo", "(NOTWORD foo)", concat(
                "not(lower(cast(fn as varchar)) like ('%' || replace(",
                "  replace(",
                "    replace(",
                "      lower('foo'), ",
                "      '!', ",
                "      '!!'",
                "    ), ",
                "    '%', ",
                "    '!%'",
                "  ), ",
                "  '_', ",
                "  '!_'",
                ") || '%') escape '!')"), CONTAINS ),
            Arguments.of(  "\"foo\"",    "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS ),
            Arguments.of( "-\"foo\"", "(NOTQUOTED foo)", "lower(cast(fn as varchar)) <> lower('foo')", EQUALS ),
            Arguments.of( "=\"foo\"",    "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS ),

            Arguments.of( "*foo",                "(OPENLEFT %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE ),
            Arguments.of( "-*foo",            "(NOTOPENLEFT %foo)", "lower(cast(fn as varchar)) not like lower('%foo')", LIKE ),
            Arguments.of( "\"*foo\"",      "(OPENLEFTQUOTED %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE ),
            Arguments.of( "-\"*foo\"",  "(NOTOPENLEFTQUOTED %foo)", "lower(cast(fn as varchar)) not like lower('%foo')", LIKE ),

            Arguments.of( "*foo*",               "(OPENLEFTRIGHT %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE ),
            Arguments.of( "-*foo*",           "(NOTOPENLEFTRIGHT %foo%)", "lower(cast(fn as varchar)) not like lower('%foo%')", LIKE ),
            Arguments.of( "\"*foo*\"",     "(OPENLEFTRIGHTQUOTED %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE ),
            Arguments.of( "-\"*foo*\"", "(NOTOPENLEFTRIGHTQUOTED %foo%)", "lower(cast(fn as varchar)) not like lower('%foo%')", LIKE ),

            Arguments.of( "foo*",               "(OPENRIGHT foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE ),
            Arguments.of( "-foo*",           "(NOTOPENRIGHT foo%)", "lower(cast(fn as varchar)) not like lower('foo%')", LIKE ),
            Arguments.of( "\"foo*\"",     "(OPENRIGHTQUOTED foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE ),
            Arguments.of( "-\"foo*\"", "(NOTOPENRIGHTQUOTED foo%)", "lower(cast(fn as varchar)) not like lower('foo%')", LIKE ),

            Arguments.of( ">\"\"", "(SOME >\"\")", concat(
                "(",
                "  fn is not null",
                "  and char_length(cast(fn as varchar)) > 0",
                ")"), LENGTH ),
            Arguments.of( "=\"\"", "(EMPTY =\"\")", concat(
                    "(",
                    "  fn is null",
                    "  or char_length(cast(fn as varchar)) = 0",
                    ")"), LENGTH ),
            Arguments.of( "-\"\"", "(RAW -\"\")", "1 = 1", NONE ),

            Arguments.of( "s/foo/", "(REGEX foo)", "fn like_regex 'foo'", REGEX ),
            Arguments.of( "-s/foo/", "(NOTREGEX foo)", "not(fn like_regex 'foo')", REGEX ),

            Arguments.of( "\"\"", "(RAW \"\")", "1 = 1", NONE )
            // @formatter:on
        );
    }

    @Override
    protected int getSearchTermType() {
        return SearchTermType.STRING.getId();
    }

    @Override
    protected StringSearchTerm makeSearchTerm(String rawSearchTerm) {
        return (StringSearchTerm) SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm);
    }

    @Override
    protected StringSearchTermEvaluator getEvaluator() {
        return new StringSearchTermEvaluator();
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1} [type {3}]")
    @MethodSource("stringParameters")
    void stringTest(String rawSearchTerm, String tokenString, String condition, MatchType type) {
        final StringSearchTerm st = makeSearchTerm(rawSearchTerm);
        assertThat(st
            .getTokens()
            .stream()
            .map(Token::toString)
            .collect(Collectors.joining())).isEqualTo(tokenString);

        assertThat(st
            .getTokens()
            .get(0).type.matchType).isEqualTo(type);

        final StringSearchTermEvaluator ste = getEvaluator();
        final Condition s = ste.evaluate(st);

        assertThat(s.toString()).isEqualTo(condition);
    }

}
