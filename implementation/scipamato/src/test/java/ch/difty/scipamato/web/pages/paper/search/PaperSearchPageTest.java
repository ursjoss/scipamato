package ch.difty.scipamato.web.pages.paper.search;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.entity.filter.SearchCondition;
import ch.difty.scipamato.entity.projection.PaperSlim;
import ch.difty.scipamato.paging.PaginationContext;
import ch.difty.scipamato.persistance.jooq.search.SearchOrderFilter;
import ch.difty.scipamato.service.CodeClassService;
import ch.difty.scipamato.service.CodeService;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.service.PaperSlimService;
import ch.difty.scipamato.service.SearchOrderService;
import ch.difty.scipamato.web.PageParameterNames;
import ch.difty.scipamato.web.pages.BasePageTest;
import ch.difty.scipamato.web.panel.result.ResultPanel;
import ch.difty.scipamato.web.panel.search.SearchOrderPanel;
import ch.difty.scipamato.web.panel.search.SearchOrderSelectorPanel;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    private static final long SEARCH_ORDER_ID = 7;

    private static final String LABEL = "Label";

    @MockBean
    private SearchOrderService searchOrderServiceMock;
    @MockBean
    private PaperSlimService paperSlimServiceMock;
    @MockBean
    private PaperService paperServiceMock;
    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService codeServiceMock;
    @Mock
    private PaperSlim paperSlimMock;
    @Mock
    private SearchOrder searchOrderMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
        when(searchOrderServiceMock.findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class))).thenReturn(Arrays.asList(searchOrder));
        when(paperSlimMock.getId()).thenReturn(41l);
        when(paperSlimMock.getDisplayValue()).thenReturn("ps");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(searchOrderServiceMock);
    }

    @Override
    protected PaperSearchPage makePage() {
        final List<SearchCondition> conditions = Arrays.asList(new SearchCondition());
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(5l);
        return new PaperSearchPage(Model.of(so));
    }

    @Override
    protected Class<PaperSearchPage> getPageClass() {
        return PaperSearchPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertSearchOrderSelectorPanel("searchOrderSelectorPanel");
        assertSearchOrderPanel("searchOrderPanel");
        assertResultPanel("resultPanel");

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    private void assertSearchOrderSelectorPanel(String b) {
        getTester().assertLabel(b + LABEL, "Saved Searches");
        getTester().assertComponent(b, SearchOrderSelectorPanel.class);
    }

    private void assertSearchOrderPanel(String b) {
        getTester().assertLabel(b + LABEL, "Search Conditions");
        getTester().assertComponent(b, SearchOrderPanel.class);
    }

    private void assertResultPanel(String b) {
        getTester().assertLabel(b + LABEL, "Search Results");
        getTester().assertComponent(b, ResultPanel.class);
    }

    @Test
    public void clickingAddSearchCondition_forwardsToPaperSearchCriteriaPageToLoadSearchOrder() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchOrderPanel:form");
        formTester.submit("addSearchCondition");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingRemoveButtonOnSearchCondition_removesSearchCondition() {
        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(6l);
        PaperSearchPage page = new PaperSearchPage(Model.of(so));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderPanel:form:searchConditions:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelDisplayValue);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(labelDisplayValue);

        // TODO test that the event is sent, and also that receiving the event adds the filter panel to the target. See also next test
        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);

        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock, times(2)).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock, times(2)).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingRemoveButtonOnResults_removesResultAndSavesSearchOrder() {
        when(searchOrderMock.getId()).thenReturn(SEARCH_ORDER_ID);

        when(paperSlimServiceMock.countBySearchOrder(eq(searchOrderMock))).thenReturn(1, 0);
        when(paperSlimServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class))).thenReturn(Arrays.asList(paperSlimMock));

        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrderMock));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        String someTextInRow = "fa fa-fw fa-ban";
        getTester().assertContains(someTextInRow);

        final String linkPath = "resultPanel:table:body:rows:1:cells:6:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(someTextInRow);

        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock, times(2)).countBySearchOrder(eq(searchOrderMock));
        verify(paperSlimServiceMock, times(1)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class));
        verify(paperSlimMock, times(2)).getId();
        verify(paperSlimMock, times(2)).getId();
        verify(searchOrderMock, times(5)).getExcludedPaperIds();
        verify(searchOrderServiceMock).saveOrUpdate(isA(SearchOrder.class));

        verify(paperSlimServiceMock, times(2)).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock, times(2)).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void constructingPage_withPageParametersHavingSearchOrderId_loadsSearchOrderFromDb() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        getTester().assertModelValue("searchOrderSelectorPanel:form:searchOrder", searchOrder);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void constructingPage_withPageParametersLackingSearchOrderId_setsFreshSearchOrder() {
        getTester().startPage(getPageClass(), new PageParameters());
        getTester().assertRenderedPage(getPageClass());

        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

}
