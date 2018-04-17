package ch.difty.scipamato.core.web.paper.search;

import static ch.difty.scipamato.core.web.CorePageParameters.SEARCH_ORDER_ID;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.*;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.projection.PaperSlim;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.OptimisticLockingException.Type;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    private static final long SO_ID = 7;

    private static final String LABEL = "Label";

    @MockBean
    private SearchOrderService searchOrderServiceMock;
    @MockBean
    private CodeClassService   codeClassServiceMock;
    @MockBean
    private CodeService        codeServiceMock;
    @Mock
    private PaperSlim          paperSlimMock;
    @Mock
    private SearchOrder        searchOrderMock, searchOrderMock2;

    private final SearchOrder searchOrder = new SearchOrder(SO_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SO_ID)).thenReturn(Optional.of(searchOrder));
        when(searchOrderServiceMock.findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class))).thenReturn(Collections.singletonList(searchOrder));
        when(paperSlimMock.getId()).thenReturn(41L);
        when(paperSlimMock.getDisplayValue()).thenReturn("ps");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(searchOrderServiceMock); // TODO reactivate
    }

    @Override
    protected PaperSearchPage makePage() {
        final List<SearchCondition> conditions = Collections.singletonList(new SearchCondition());
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(5L);
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
        PageParameters pp = new PageParameters().add(SEARCH_ORDER_ID.getName(), SO_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchOrderPanel:form");
        formTester.submit("addSearchCondition");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(searchOrderServiceMock).findById(SO_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void clickingRemoveButtonOnSearchCondition_removesSearchCondition() {
        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Collections.singletonList(sc);
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(6L);
        PaperSearchPage page = new PaperSearchPage(Model.of(so));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderPanel:form:searchConditions:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelDisplayValue);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(labelDisplayValue);

        getTester().assertComponentOnAjaxResponse("searchOrderSelectorPanel");
        getTester().assertComponentOnAjaxResponse("searchOrderPanel");
        getTester().assertComponentOnAjaxResponse("resultPanelLabel");
        getTester().assertComponentOnAjaxResponse("resultPanel");

        // TODO test that the event is sent, and also that receiving the event adds the
        // filter panel to the target. See also next test
        verify(searchOrderServiceMock, never()).findById(SO_ID);

        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(paperSlimServiceMock, times(2)).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock, times(3)).findPageOfIdsBySearchOrder(isA(SearchOrder.class),
            isA(PaginationContext.class));
    }

    @Test
    public void clickingNewSearchCondition_reloadsPage() {
        when(searchOrderServiceMock.saveOrUpdate(isA(SearchOrder.class))).thenReturn(searchOrderMock);
        when(searchOrderMock.getId()).thenReturn(27L);
        when(searchOrderServiceMock.findById(27L)).thenReturn(Optional.of(searchOrderMock2));

        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Collections.singletonList(sc);
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(6L);
        PaperSearchPage page = new PaperSearchPage(Model.of(so));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderSelectorPanel:form:new";
        getTester().assertComponent(linkPath, AjaxSubmitLink.class);
        getTester().clickLink(linkPath);

        getTester().assertRenderedPage(PaperSearchPage.class);

        verify(searchOrderServiceMock).saveOrUpdate(isA(SearchOrder.class));
        verify(searchOrderMock).getId();
        verify(searchOrderServiceMock).findById(27L);
        verify(searchOrderMock2).setShowExcluded(false);
        verify(searchOrderServiceMock, times(3)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
    }

    @Test
    public void clickingNewSearchCondition_withOptimisticLockingException_failsSaveAndWarns() {
        when(searchOrderMock.getId()).thenReturn(27L);
        when(searchOrderServiceMock.saveOrUpdate(isA(SearchOrder.class))).thenThrow(
            new OptimisticLockingException("searchOrder", "record", Type.UPDATE));

        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Collections.singletonList(sc);
        final SearchOrder so = new SearchOrder(conditions);
        so.setId(6L);
        PaperSearchPage page = new PaperSearchPage(Model.of(so));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderSelectorPanel:form:new";
        getTester().assertComponent(linkPath, AjaxSubmitLink.class);
        getTester().clickLink(linkPath);

        getTester().assertErrorMessages(
            "The searchOrder with id 6 has been modified concurrently by another user. Please reload it and apply your changes once more.");
        getTester().assertRenderedPage(PaperSearchPage.class);

        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(searchOrderServiceMock).saveOrUpdate(isA(SearchOrder.class));
    }

    @Test
    public void clickingRemoveButtonOnResults_removesResultAndSavesSearchOrder() {
        when(searchOrderMock.getId()).thenReturn(SO_ID);

        when(paperSlimServiceMock.countBySearchOrder(eq(searchOrderMock))).thenReturn(1, 0);
        when(paperSlimServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class))).thenReturn(
            Collections.singletonList(paperSlimMock));
        when(searchOrderServiceMock.saveOrUpdate(isA(SearchOrder.class))).thenReturn(searchOrderMock2);

        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrderMock));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        String someTextInRow = "fa fa-fw fa-ban";
        getTester().assertContains(someTextInRow);

        final String linkPath = "resultPanel:table:body:rows:1:cells:6:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(someTextInRow);

        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(paperSlimServiceMock, times(1)).countBySearchOrder(eq(searchOrderMock));
        verify(paperSlimServiceMock, times(1)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class));
        verify(paperSlimMock, times(2)).getId();
        verify(paperSlimMock, times(2)).getId();
        verify(searchOrderMock, times(3)).getExcludedPaperIds();
        verify(searchOrderServiceMock).saveOrUpdate(searchOrderMock);

        verify(paperSlimServiceMock, times(1)).countBySearchOrder(searchOrderMock2);
        verify(paperServiceMock, times(2)).findPageOfIdsBySearchOrder(eq(searchOrderMock2),
            isA(PaginationContext.class));
    }

    @Test
    public void constructingPage_withPageParametersHavingSearchOrderId_loadsSearchOrderFromDb() {
        PageParameters pp = new PageParameters().add(SEARCH_ORDER_ID.getName(), SO_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        getTester().assertModelValue("searchOrderSelectorPanel:form:searchOrder", searchOrder);

        verify(searchOrderServiceMock).findById(SO_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void constructingPage_withPageParametersLackingSearchOrderId_setsFreshSearchOrder() {
        getTester().startPage(getPageClass(), new PageParameters());
        getTester().assertRenderedPage(getPageClass());

        verify(searchOrderServiceMock, never()).findById(SO_ID);

        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
        verify(paperSlimServiceMock).countBySearchOrder(isA(SearchOrder.class));
        verify(paperServiceMock).findPageOfIdsBySearchOrder(isA(SearchOrder.class), isA(PaginationContext.class));
    }

    @Test
    public void searchOrderMock_withNoExclusions_hidesShowExcludedButton() {
        when(searchOrderMock.getId()).thenReturn(SO_ID);
        when(searchOrderMock.getExcludedPaperIds()).thenReturn(new ArrayList<>());

        when(paperSlimServiceMock.countBySearchOrder(eq(searchOrderMock))).thenReturn(1, 0);
        when(paperSlimServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class))).thenReturn(
            Collections.singletonList(paperSlimMock));
        when(searchOrderServiceMock.saveOrUpdate(isA(SearchOrder.class))).thenReturn(searchOrderMock2);

        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrderMock));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderSelectorPanel:form:showExcluded";
        getTester().assertInvisible(linkPath);

        verify(searchOrderServiceMock, times(1)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(paperSlimServiceMock, times(1)).countBySearchOrder(eq(searchOrderMock));
        verify(paperSlimServiceMock, times(1)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class));
        verify(paperSlimMock, times(1)).getId();
        verify(searchOrderMock, times(3)).getExcludedPaperIds();
    }

    @Test
    public void searchOrderMock_withExclusions_whenClicking_sendsEvent() {
        when(searchOrderMock.getId()).thenReturn(SO_ID);
        when(searchOrderMock.getExcludedPaperIds()).thenReturn(Arrays.asList(5L, 3L));

        when(paperSlimServiceMock.countBySearchOrder(eq(searchOrderMock))).thenReturn(1, 0);
        when(paperSlimServiceMock.findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class))).thenReturn(
            Collections.singletonList(paperSlimMock));
        when(searchOrderServiceMock.saveOrUpdate(isA(SearchOrder.class))).thenReturn(searchOrderMock2);

        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrderMock));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderSelectorPanel:form:showExcluded";
        getTester().assertComponent(linkPath, AjaxCheckBox.class);
        getTester().executeAjaxEvent(linkPath, "click");

        getTester().assertComponentOnAjaxResponse("resultPanelLabel");
        getTester().assertComponentOnAjaxResponse("resultPanel");

        verify(searchOrderServiceMock, times(1)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(paperSlimServiceMock, atLeast(1)).countBySearchOrder(eq(searchOrderMock));
        verify(paperSlimServiceMock, times(1)).findPageBySearchOrder(eq(searchOrderMock), isA(PaginationContext.class));
        verify(paperSlimMock, atLeast(1)).getId();
        verify(searchOrderMock, times(6)).getExcludedPaperIds();
    }

}
