package ch.difty.sipamato.web.pages.paper.search;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.pages.BasePageTest;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

public class PaperSearchCriteriaPageTest extends BasePageTest<PaperSearchCriteriaPage> {

    private static final long SEARCH_ORDER_ID = 5;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @Mock
    private SearchCondition searchConditionMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, 1, false, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
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
    public void submittingForm_savesSearchConditionAndforwardsToPaperSearchPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit();

        getTester().assertRenderedPage(PaperSearchPage.class);

        verify(searchOrderServiceMock).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID);
    }

}
