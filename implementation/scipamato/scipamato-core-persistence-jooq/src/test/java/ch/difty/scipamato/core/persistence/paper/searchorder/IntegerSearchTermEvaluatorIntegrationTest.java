package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.Condition;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.search.IntegerSearchTerm;
import ch.difty.scipamato.core.entity.search.IntegerSearchTerm.MatchType;
import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;

/**
 * Test class to integration test the search term and the search term evaluator.
 */
class IntegerSearchTermEvaluatorIntegrationTest extends SearchTermEvaluatorIntegrationTest<IntegerSearchTerm> {

    @SuppressWarnings("unused")
    private Object[] integerParameters() {
        return new Object[] {
            // @formatter:off
            new Object[] { "<2016", 2016, 2016, LESS_THAN, "fn < 2016" },
            new Object[] { "<=2016", 2016, 2016, LESS_OR_EQUAL, "fn <= 2016" },
            new Object[] { "2016", 2016, 2016, EXACT, "fn = 2016" },
            new Object[] { "=2016", 2016, 2016, EXACT, "fn = 2016" },
            new Object[] { ">2016", 2016, 2016, GREATER_THAN, "fn > 2016" },
            new Object[] { ">=2016", 2016, 2016, GREATER_OR_EQUAL, "fn >= 2016" },
            new Object[] { "2016-2018", 2016, 2018, RANGE, "fn between 2016 and 2018" },
            // @formatter:on
        };
    }

    @Override
    protected int getSearchTermType() {
        return SearchTermType.INTEGER.getId();
    }

    @Override
    protected IntegerSearchTerm makeSearchTerm(String rawSearchTerm) {
        return (IntegerSearchTerm) SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm);
    }

    @Override
    protected IntegerSearchTermEvaluator getEvaluator() {
        return new IntegerSearchTermEvaluator();
    }

    // TODO restore parametrized test
//    @Test
//    @Parameters(method = "integerParameters")
    public void integerTest(String rawSearchTerm, int value, int value2, MatchType type, String condition) {
        final IntegerSearchTerm st = makeSearchTerm(rawSearchTerm);
        assertThat(st.getValue()).isEqualTo(value);
        assertThat(st.getValue2()).isEqualTo(value2);
        assertThat(st.getType()).isEqualTo(type);

        final IntegerSearchTermEvaluator ste = getEvaluator();
        final Condition s = ste.evaluate(st);

        assertThat(s.toString()).isEqualTo(condition);
    }

}
