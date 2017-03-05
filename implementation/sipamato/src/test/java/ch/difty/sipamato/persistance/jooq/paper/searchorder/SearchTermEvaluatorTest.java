package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.filter.SearchTerm;
import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public abstract class SearchTermEvaluatorTest<T extends SearchTerm> {

    // needs to match the newlines used in JOOQ Condition.toString()
    private static final String NL = "\n";

    protected abstract SearchTermEvaluator<T> getEvaluator();

    @Test
    public void evaluating_withNullParameter_throws() {
        try {
            getEvaluator().evaluate(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("searchTerm must not be null.");
        }
    }

    protected String concat(String... strings) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : strings) {
            sb.append(s).append(NL);
        }
        if (sb.length() > NL.length()) {
            return sb.substring(0, sb.length() - NL.length());
        } else {
            return sb.toString();
        }
    }
}
