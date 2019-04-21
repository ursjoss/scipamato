package ch.difty.scipamato.core.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.SortProperty;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;

public class SearchOrderModelTest extends ModelTest {

    @MockBean
    private SearchOrderService serviceMock;
    @Mock
    private SearchOrder        mockSearchOrder;

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, mockSearchOrder);
    }

    @Test
    public void test() {
        final int owner = 1;
        final int maxRows = 10;
        when(serviceMock.findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationRequest.class))).thenReturn(
            Arrays.asList(mockSearchOrder, mockSearchOrder));
        SearchOrderModel m = new SearchOrderModel(owner, maxRows);
        assertThat(m.load()).containsExactly(mockSearchOrder, mockSearchOrder);

        verify(serviceMock).findPageByFilter(Mockito.argThat(new SearchOrderFilterMatcher(owner)),
            Mockito.argThat(new PaginationRequestWithMaxRows(maxRows)));
    }

    @SuppressWarnings("SameParameterValue")
    class SearchOrderFilterMatcher implements ArgumentMatcher<SearchOrderFilter> {
        private int ownerIncludingGlobal;

        SearchOrderFilterMatcher(int ownerIncludingGlobal) {
            this.ownerIncludingGlobal = ownerIncludingGlobal;
        }

        @Override
        public boolean matches(SearchOrderFilter f) {
            return f != null && f.getOwnerIncludingGlobal() != null
                   && f.getOwnerIncludingGlobal() == ownerIncludingGlobal && f.getNameMask() == null
                   && f.getOwner() == null && f.getGlobal() == null;
        }
    }

    class PaginationRequestWithMaxRows implements ArgumentMatcher<PaginationRequest> {
        private static final String GLOBAL = "global";

        private int maxRows;

        PaginationRequestWithMaxRows(int maxRows) {
            this.maxRows = maxRows;
        }

        @Override
        public boolean matches(PaginationRequest pr) {
            if (pr != null && pr.getOffset() == 0 && pr.getPageSize() == maxRows) {
                Iterator<SortProperty> it = pr
                    .getSort()
                    .iterator();
                if (it.hasNext()) {
                    SortProperty sp = it.next();
                    return GLOBAL.equals(sp.getName()) && sp
                        .getDirection()
                        .isAscending();
                }
            }
            return false;
        }
    }
}
