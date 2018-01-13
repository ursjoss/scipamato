package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.CONTAINS;
import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.EQUALS;
import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.LENGTH;
import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.LIKE;
import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.NONE;
import static ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType.REGEX;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import org.jooq.Condition;
import org.junit.Test;

import ch.difty.scipamato.core.entity.filter.SearchTerm;
import ch.difty.scipamato.core.entity.filter.SearchTermType;
import ch.difty.scipamato.core.entity.filter.StringSearchTerm;
import ch.difty.scipamato.core.entity.filter.StringSearchTerm.MatchType;
import ch.difty.scipamato.core.entity.filter.StringSearchTerm.Token;
import junitparams.Parameters;

/**
 * Test class to integration test the search term and the search term evaluator.
 */
public class StringSearchTermEvaluatorIntegrationTest extends SearchTermEvaluatorIntegrationTest<StringSearchTerm> {

    @SuppressWarnings("unused")
    private Object[] stringParameters() {
        return new Object[] {
            // @formatter:off
            new Object[] { "foo", "(WORD foo)", concat(
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
                ") || '%') escape '!'"), CONTAINS },
            new Object[] { "-foo", "(NOTWORD foo)", concat(
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
                ") || '%') escape '!')"), CONTAINS },
            new Object[] {  "\"foo\"",    "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS },
            new Object[] { "-\"foo\"", "(NOTQUOTED foo)", "lower(cast(fn as varchar)) <> lower('foo')", EQUALS },
            new Object[] { "=\"foo\"",    "(QUOTED foo)", "lower(cast(fn as varchar)) = lower('foo')", EQUALS },

            new Object[] { "*foo",                "(OPENLEFT %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE },
            new Object[] { "-*foo",            "(NOTOPENLEFT %foo)", "lower(cast(fn as varchar)) not like lower('%foo')", LIKE },
            new Object[] { "\"*foo\"",      "(OPENLEFTQUOTED %foo)", "lower(cast(fn as varchar)) like lower('%foo')", LIKE },
            new Object[] { "-\"*foo\"",  "(NOTOPENLEFTQUOTED %foo)", "lower(cast(fn as varchar)) not like lower('%foo')", LIKE },

            new Object[] { "*foo*",               "(OPENLEFTRIGHT %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE },
            new Object[] { "-*foo*",           "(NOTOPENLEFTRIGHT %foo%)", "lower(cast(fn as varchar)) not like lower('%foo%')", LIKE },
            new Object[] { "\"*foo*\"",     "(OPENLEFTRIGHTQUOTED %foo%)", "lower(cast(fn as varchar)) like lower('%foo%')", LIKE },
            new Object[] { "-\"*foo*\"", "(NOTOPENLEFTRIGHTQUOTED %foo%)", "lower(cast(fn as varchar)) not like lower('%foo%')", LIKE },

            new Object[] { "foo*",               "(OPENRIGHT foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE },
            new Object[] { "-foo*",           "(NOTOPENRIGHT foo%)", "lower(cast(fn as varchar)) not like lower('foo%')", LIKE },
            new Object[] { "\"foo*\"",     "(OPENRIGHTQUOTED foo%)", "lower(cast(fn as varchar)) like lower('foo%')", LIKE },
            new Object[] { "-\"foo*\"", "(NOTOPENRIGHTQUOTED foo%)", "lower(cast(fn as varchar)) not like lower('foo%')", LIKE },

            new Object[] { ">\"\"", "(SOME >\"\")", concat(
                "(",
                "  fn is not null",
                "  and char_length(cast(fn as varchar)) > 0",
                ")"), LENGTH },
            new Object[] { "=\"\"", "(EMPTY =\"\")", concat(
                    "(",
                    "  fn is null",
                    "  or char_length(cast(fn as varchar)) = 0",
                    ")"), LENGTH },
            new Object[] { "-\"\"", "(RAW -\"\")", "1 = 1", NONE },

            new Object[] { "s/foo/", "(REGEX foo)", "fn like_regex 'foo'", REGEX },
            new Object[] { "-s/foo/", "(NOTREGEX foo)", "not(fn like_regex 'foo')", REGEX },

            new Object[] { "\"\"", "(RAW \"\")", "1 = 1", NONE },
            // @formatter:on
        };
    }

    @Override
    protected int getSearchTermType() {
        return SearchTermType.STRING.getId();
    }

    @Override
    protected StringSearchTerm makeSearchTerm(String rawSearchTerm) {
        return (StringSearchTerm) SearchTerm.of(ID, searchTermType, SC_ID, FN, rawSearchTerm);
    }

    @Override
    protected StringSearchTermEvaluator getEvaluator() {
        return new StringSearchTermEvaluator();
    }

    @Test
    @Parameters(method = "stringParameters")
    public void stringTest(String rawSearchTerm, String tokenString, String condition, MatchType type) {
        final StringSearchTerm st = makeSearchTerm(rawSearchTerm);
        assertThat(st.getTokens()
            .stream()
            .map(Token::toString)
            .collect(Collectors.joining())).isEqualTo(tokenString);

        assertThat(st.getTokens()
            .get(0).type.matchType).isEqualTo(type);

        final StringSearchTermEvaluator ste = getEvaluator();
        final Condition s = ste.evaluate(st);

        assertThat(s.toString()).isEqualTo(condition);
    }

}
