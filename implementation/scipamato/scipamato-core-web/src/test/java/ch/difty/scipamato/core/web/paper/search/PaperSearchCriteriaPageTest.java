package ch.difty.scipamato.core.web.paper.search;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.CodeClassService;
import ch.difty.scipamato.core.persistence.CodeService;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.common.SearchablePaperPanel;

public class PaperSearchCriteriaPageTest extends BasePageTest<PaperSearchCriteriaPage> {

    private static final long SEARCH_ORDER_ID = 5;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @MockBean
    private CodeService codeServiceMock;

    @MockBean
    private CodeClassService codeClassServiceMock;

    @Mock
    private SearchCondition searchConditionMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        when(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
        when(searchOrderServiceMock.findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class))).thenReturn(Collections.singletonList(searchOrder));
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

        verify(searchOrderServiceMock).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID, "en_us");
        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
    }

}
