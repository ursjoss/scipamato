package ch.difty.scipamato.core.web.paper.search;

import static com.nhaarman.mockitokotlin2.OngoingStubbingKt.whenever;
import static com.nhaarman.mockitokotlin2.VerificationKt.times;
import static com.nhaarman.mockitokotlin2.VerificationKt.verify;
import static org.mockito.ArgumentMatchers.isA;

import java.util.Collections;
import java.util.Optional;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.common.SearchablePaperPanel;

class PaperSearchCriteriaPageTest extends BasePageTest<PaperSearchCriteriaPage> {

    private static final long SEARCH_ORDER_ID = 5;

    @Mock
    private SearchCondition searchConditionMock;

    private final SearchOrder searchOrder = new SearchOrder(SEARCH_ORDER_ID, "soName", 1, false, null, null);

    @Override
    protected void setUpHook() {
        whenever(searchOrderServiceMock.findById(SEARCH_ORDER_ID)).thenReturn(Optional.of(searchOrder));
        whenever(searchOrderServiceMock.findPageByFilter(isA(SearchOrderFilter.class),
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

    @SuppressWarnings("SameParameterValue")
    private void assertForm(String b) {
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":firstAuthorOverridden", CheckBox.class);
    }

    @Test
    void submittingForm_savesSearchCondition_andRemainsOnPagePage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit();

        getTester().assertRenderedPage(getPageClass());
        getTester().assertNoErrorMessage();

        verify(searchOrderServiceMock).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID, "en_us");
        verify(searchOrderServiceMock, times(0)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
    }

    @Test
    void submittingForm_andClickingSubmitButton_savesSearchConditionAndForwardsToPaperSearchPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit("submit");

        getTester().assertRenderedPage(PaperSearchPage.class);
        getTester().assertNoErrorMessage();

        verify(searchOrderServiceMock).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID, "en_us");
        verify(searchOrderServiceMock).findPageByFilter(isA(SearchOrderFilter.class), isA(PaginationContext.class));
    }

    @Test
    void submittingForm_withErrorInService_addsErrorMessage() {
        whenever(searchOrderServiceMock.saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID,
            "en_us")).thenThrow(new RuntimeException("foo"));

        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        FormTester formTester = getTester().newFormTester("contentPanel:form");
        formTester.submit();

        getTester().assertErrorMessages("An unexpected error occurred when trying to save Search Order [id ]: foo",
            "An unexpected error occurred when trying to save Search Order [id ]: foo");
        getTester().assertRenderedPage(getPageClass());

        verify(searchOrderServiceMock, times(2)).saveOrUpdateSearchCondition(searchConditionMock, SEARCH_ORDER_ID,
            "en_us");
        verify(searchOrderServiceMock, times(0)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
    }

}
