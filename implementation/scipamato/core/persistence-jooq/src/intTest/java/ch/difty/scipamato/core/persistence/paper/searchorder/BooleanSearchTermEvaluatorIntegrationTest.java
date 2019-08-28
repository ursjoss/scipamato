package ch.difty.scipamato.core.persistence.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.jooq.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import ch.difty.scipamato.core.entity.search.BooleanSearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTerm;
import ch.difty.scipamato.core.entity.search.SearchTermType;

/**
 * Test class to integration test the search term and the search term evaluator.
 */
@JooqTest
@Testcontainers
class BooleanSearchTermEvaluatorIntegrationTest extends SearchTermEvaluatorIntegrationTest<BooleanSearchTerm> {

    private static Stream<Arguments> booleanParameters() {
        return Stream.of(
            // @formatter:off
            Arguments.of("true", true, "fn = true" ),
            Arguments.of( "false", false, "fn = false")
            // @formatter:on
        );
    }

    @Override
    protected int getSearchTermType() {
        return SearchTermType.BOOLEAN.getId();
    }

    @Override
    protected BooleanSearchTerm makeSearchTerm(String rawSearchTerm) {
        return (BooleanSearchTerm) SearchTerm.newSearchTerm(ID, searchTermType, SC_ID, FN, rawSearchTerm);
    }

    @Override
    protected BooleanSearchTermEvaluator getEvaluator() {
        return new BooleanSearchTermEvaluator();
    }

    @ParameterizedTest(name = "[{index}] {0} -> {1} ({4})")
    @MethodSource("booleanParameters")
    void booleanTest(String rawSearchTerm, Boolean value, String condition) {
        final BooleanSearchTerm st = makeSearchTerm(rawSearchTerm);
        assertThat(st.getValue()).isEqualTo(value);

        final BooleanSearchTermEvaluator ste = getEvaluator();
        final Condition s = ste.evaluate(st);

        assertThat(s.toString()).isEqualTo(condition);
    }

}
