package ch.difty.sipamato.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.PaginationRequest;
import ch.difty.sipamato.paging.Sort.Order;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;
import ch.difty.sipamato.service.SearchOrderService;

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
        private int maxRows;

        public PaginationRequestWithMaxRows(int maxRows) {
            this.maxRows = maxRows;
        }

        @Override
        public boolean matches(Object arg) {
            if (arg != null && arg instanceof PaginationRequest) {
                PaginationRequest pr = (PaginationRequest) arg;
                if (pr.getOffset() == 0 && pr.getPageSize() == maxRows) {
                    Iterator<Order> it = pr.getSort().iterator();
                    if (it.hasNext()) {
                        Order o = it.next();
                        return "global".equals(o.getProperty()) && o.getDirection().isAscending();
                    }
                }
            }
            return false;
        }
    }
}
