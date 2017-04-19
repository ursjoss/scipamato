package ch.difty.sipamato.web.pages.paper.search;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.paging.Page;
import ch.difty.sipamato.paging.Pageable;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;
import ch.difty.sipamato.service.CodeClassService;
import ch.difty.sipamato.service.CodeService;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

public class PaperSearchCriteriaPageTest extends BasePageTest<PaperSearchCriteriaPage> {

    private static final long SEARCH_ORDER_ID = 5;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @MockBean
    private PaperSlimService serviceMock;

    @MockBean
    private CodeService codeServiceMock;

    @MockBean
    private CodeClassService codeClassServiceMock;

    @Mock
    private SearchCondition searchConditionMock;
    @Mock
    private Page<SearchOrder> searchOrderPageMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
        when(searchOrderServiceMock.findByFilter(isA(SearchOrderFilter.class), isA(Pageable.class))).thenReturn(searchOrderPageMock);
        when(searchOrderPageMock.getContent()).thenReturn(Arrays.asList(searchOrder));
    }

    @Override
    protected PaperSearchCriteriaPage makePage() {
        return new PaperSearchCriteriaPage(Model.of(searchConditionMock), SEARCH_ORDER_ID);
    }

    @Override
    protected Class<PaperSearchCriteriaPage> getPageClass() {
        return PaperSearchCriteriaPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        getTester().assertComponent("contentPanel", SearchablePaperPanel.class);
        assertForm("contentPanel:form");
    }

    private void assertForm(String b) {
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":firstAuthorOverridden", CheckBox.class);
    }

    @Test
    public void submittingForm_savesSearchConditionAndForwardsToPaperSearchPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit();

        getTester().assertRenderedPage(PaperSearchPage.class);

        verify(searchOrderServiceMock).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID);
        verify(searchOrderServiceMock).findByFilter(isA(SearchOrderFilter.class), isA(Pageable.class));
        verify(searchOrderPageMock).getContent();
    }

}
