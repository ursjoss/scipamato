package ch.difty.scipamato.web.model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchOrderFilter;
import ch.difty.scipamato.persistence.SearchOrderService;
import ch.difty.scipamato.persistence.paging.PaginationRequest;
import ch.difty.scipamato.persistence.paging.Sort.SortProperty;

public class SearchOrderModelTest extends ModelTest {

    @MockBean
    private SearchOrderService serviceMock;
    @Mock
    private SearchOrder mockSearchOrder;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, mockSearchOrder);
    }

    @Test
    public void test() {
        final int owner = 1;
        final int maxRows = 10;
        when(serviceMock.findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationRequest.class))).thenReturn(Arrays.asList(mockSearchOrder, mockSearchOrder));
        SearchOrderModel m = new SearchOrderModel(owner, maxRows);
        assertThat(m.load()).containsExactly(mockSearchOrder, mockSearchOrder);

        verify(serviceMock).findPageByFilter(argThat(new SearchOrderFilterMatcher(owner)), argThat(new PaginationRequestWithMaxRows(maxRows)));
    }

    protected class SearchOrderFilterMatcher extends ArgumentMatcher<SearchOrderFilter> {
        private int ownerIncludingGlobal;

        public SearchOrderFilterMatcher(int ownerIncludingGlobal) {
            this.ownerIncludingGlobal = ownerIncludingGlobal;
        }

        @Override
        public boolean matches(Object arg) {
            if (arg != null && arg instanceof SearchOrderFilter) {
                SearchOrderFilter f = (SearchOrderFilter) arg;
                return f.getOwnerIncludingGlobal() != null && f.getOwnerIncludingGlobal().intValue() == ownerIncludingGlobal && f.getNameMask() == null && f.getOwner() == null
                        && f.getGlobal() == null;
            }
            return false;
        }
    }

    protected class PaginationRequestWithMaxRows extends ArgumentMatcher<PaginationRequest> {
        private static final String GLOBAL = "global";

        private int maxRows;

        public PaginationRequestWithMaxRows(int maxRows) {
            this.maxRows = maxRows;
        }

        @Override
        public boolean matches(Object arg) {
            if (arg != null && arg instanceof PaginationRequest) {
                PaginationRequest pr = (PaginationRequest) arg;
                if (pr.getOffset() == 0 && pr.getPageSize() == maxRows) {
                    Iterator<SortProperty> it = pr.getSort().iterator();
                    if (it.hasNext()) {
                        SortProperty sp = it.next();
                        return GLOBAL.equals(sp.getName()) && sp.getDirection().isAscending();
                    }
                }
            }
            return false;
        }
    }
}
