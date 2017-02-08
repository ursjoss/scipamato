package ch.difty.sipamato.persistance.jooq.paper.searchorder;

import org.junit.runner.RunWith;

import ch.difty.sipamato.entity.filter.SearchTerm;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public abstract class SearchTermEvaluatorIntegrationTest<T extends SearchTerm, E extends SearchTermEvaluator<T>> {

    protected static final long ID = 1;
    protected static final long SC_ID = 10;
    protected static final String FN = "fn";
    protected int searchTermType = getSearchTermType();

    protected abstract int getSearchTermType();

    protected abstract T makeSearchTerm(String rawSearchTerm);

    protected abstract E makeSearchTermEvaluator();

    protected String concat(String... strings) {
        final StringBuilder sb = new StringBuilder();
        final String nl = System.getProperty("line.separator");
        for (final String s : strings) {
            sb.append(s).append(nl);
        }
        if (sb.length() > nl.length()) {
            return sb.substring(0, sb.length() - nl.length());
        } else {
            return sb.toString();
        }
    }

}
