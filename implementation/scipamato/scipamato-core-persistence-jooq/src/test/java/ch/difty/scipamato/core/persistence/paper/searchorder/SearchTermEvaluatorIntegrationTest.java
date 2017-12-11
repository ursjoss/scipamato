package ch.difty.scipamato.core.persistence.paper.searchorder;

import org.junit.runner.RunWith;

import ch.difty.scipamato.core.entity.filter.SearchTerm;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public abstract class SearchTermEvaluatorIntegrationTest<T extends SearchTerm> extends SearchTermEvaluatorTest<T> {

    protected static final long ID = 1;
    protected static final long SC_ID = 10;
    protected static final String FN = "fn";
    protected int searchTermType = getSearchTermType();

    protected abstract int getSearchTermType();

    protected abstract T makeSearchTerm(String rawSearchTerm);

}
