package ch.difty.scipamato.core.persistence.paper.searchorder;

import static ch.difty.scipamato.common.TestUtilsKt.assertDegenerateSupplierParameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.search.SearchTerm;

@ExtendWith(MockitoExtension.class)
public abstract class SearchTermEvaluatorTest<T extends SearchTerm> {

    // needs to match the newlines used in JOOQ Condition.toString()
    private static final String NL = "\n";

    protected abstract SearchTermEvaluator<T> getEvaluator();

    @Test
    void evaluating_withNullParameter_throws() {
        assertDegenerateSupplierParameter(() -> getEvaluator().evaluate(null), "searchTerm");
    }

    static String concat(String... strings) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : strings) {
            sb
                .append(s)
                .append(NL);
        }
        if (sb.length() > NL.length()) {
            return sb.substring(0, sb.length() - NL.length());
        } else {
            return sb.toString();
        }
    }
}
