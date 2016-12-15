package ch.difty.sipamato.web.pages.paper.search;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.PageParameterNames;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderSelectorPanel;

public class PaperSearchPageTest extends BasePageTest<PaperSearchPage> {

    private static final long SEARCH_ORDER_ID = 7;

    @MockBean
    private SearchOrderService searchOrderServiceMock;
    @MockBean
    private PaperSlimService paperSlimServiceMock;
    @MockBean
    private CodeClassService codeClassServiceMock;
    @MockBean
    private CodeService codeServiceMock;
    @Mock
    private PaperSlim paperSlimMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private Page<PaperSlim> pageMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
    }

    @Override
    protected PaperSearchPage makePage() {
        final List<SearchCondition> conditions = Arrays.asList(new SearchCondition());
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(5l);
        return new PaperSearchPage(Model.of(searchOrder));
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
    }

    private void assertSearchOrderSelectorPanel(String b) {
        getTester().assertLabel(b + "Label", "Saved Searches");
        getTester().assertComponent(b, SearchOrderSelectorPanel.class);
    }

    private void assertSearchOrderPanel(String b) {
        getTester().assertLabel(b + "Label", "Search Conditions");
        getTester().assertComponent(b, SearchOrderPanel.class);
    }

    private void assertResultPanel(String b) {
        getTester().assertLabel(b + "Label", "Search Results");
        getTester().assertComponent(b, ResultPanel.class);
    }

    @Test
    public void clickingAddSearch_forwardsToPaperSearchCriteriaPageToLoadSearchOrder() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("searchOrderPanel:form");
        formTester.submit("addSearch");

        getTester().assertRenderedPage(PaperSearchCriteriaPage.class);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void clickingRemoveButtonOnSearchTerms_removesSearchTerm() {
        final String labelDisplayValue = "searchConditionDisplayValue";
        final SearchCondition sc = new SearchCondition() {
            private static final long serialVersionUID = 1L;

            public String getDisplayValue() {
                return labelDisplayValue;
            }
        };
        final List<SearchCondition> conditions = Arrays.asList(sc);
        final SearchOrder searchOrder = new SearchOrder(conditions);
        searchOrder.setId(6l);
        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrder));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        final String linkPath = "searchOrderPanel:form:searchTerms:body:rows:1:cells:2:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().assertContains(labelDisplayValue);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(labelDisplayValue);

        // TODO test that the event is sent
        // TODO also test that receiving the event adds the filter panel to the target
        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void clickingRemoveButtonOnResults_removesResults() {
        when(searchOrderMock.getId()).thenReturn(SEARCH_ORDER_ID);

        when(paperSlimServiceMock.countBySearchOrder(Mockito.eq(searchOrderMock))).thenReturn(1);
        when(paperSlimServiceMock.findBySearchOrder(Mockito.eq(searchOrderMock), Mockito.isA(Pageable.class))).thenReturn(pageMock);
        when(pageMock.iterator()).thenReturn(Arrays.asList(paperSlimMock).iterator());

        PaperSearchPage page = new PaperSearchPage(Model.of(searchOrderMock));

        getTester().startPage(page);
        getTester().assertRenderedPage(getPageClass());

        String someTextInPage = "fa fa-fw fa-trash-o text-danger";
        getTester().assertContains(someTextInPage);

        final String linkPath = "resultPanel:table:body:rows:1:cells:5:cell:link";
        getTester().assertComponent(linkPath, AjaxLink.class);
        getTester().clickLink(linkPath);
        getTester().assertContainsNot(someTextInPage);
        // TODO test that the event is sent
        verify(paperSlimServiceMock, times(2)).countBySearchOrder(Mockito.eq(searchOrderMock));
        verify(paperSlimServiceMock, times(2)).findBySearchOrder(Mockito.eq(searchOrderMock), Mockito.isA(Pageable.class));
        verify(pageMock, times(2)).iterator();
    }

    @Test
    public void constructingPage_withPageParametersHavingSearchOrderId_loadsSearchOrderFromDb() {
        PageParameters pp = new PageParameters().add(PageParameterNames.SEARCH_ORDER_ID, SEARCH_ORDER_ID);
        getTester().startPage(getPageClass(), pp);
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        getTester().assertModelValue("searchOrderSelectorPanel:form:searchOrder", searchOrder);

        verify(searchOrderServiceMock).findById(SEARCH_ORDER_ID);
    }

    @Test
    public void constructingPage_withPageParametersLackingSearchOrderId_setsFreshSearchOrder() {
        getTester().startPage(getPageClass(), new PageParameters());
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();

        verify(searchOrderServiceMock, never()).findById(SEARCH_ORDER_ID);
    }

}
