package ch.difty.scipamato.core.web.paper.search;

import static ch.difty.scipamato.core.entity.search.SearchOrder.SearchOrderFields.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.entity.search.SearchOrderFilter;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.common.PanelTest;

public class SearchOrderSelectorPanelTest extends PanelTest<SearchOrderSelectorPanel> {

    private static final long   ID         = 17L;
    private static final String VALID_NAME = "soName";
    private static final int    OWNER_ID   = 2;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private SearchOrder searchOrderMock2;

    private       List<SearchOrder>     searchOrders;
    private final List<SearchCondition> searchConditions = new ArrayList<>();

    @Override
    protected SearchOrderSelectorPanel makePanel() {
        return new SearchOrderSelectorPanel(PANEL_ID, Model.of(searchOrderMock));
    }

    @Override
    protected void setUpHook() {
        super.setUpHook();

        searchOrders = Arrays.asList(searchOrderMock,
            new SearchOrder(20L, "soName", OWNER_ID, true, searchConditions, null));
        when(searchOrderServiceMock.findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class))).thenReturn(searchOrders);
        when(searchOrderMock.getId()).thenReturn(ID);
    }

    @Override
    protected void assertSpecificComponents() {
        String b = PANEL_ID;
        getTester().assertComponent(b, Panel.class);
        assertForm(b + ":form");
    }

    private void assertForm(String b) {
        getTester().assertComponent(b, Form.class);

        getTester().assertComponent(b + ":searchOrder", BootstrapSelect.class);
        getTester().assertLabel(b + ":nameLabel", "Name");
        getTester().assertComponent(b + ":name", TextField.class);
        getTester().assertLabel(b + ":globalLabel", "Global");
        getTester().assertComponent(b + ":global", CheckBoxX.class);
        getTester().assertComponent(b + ":new", AjaxSubmitLink.class);
        getTester().assertComponent(b + ":delete", AjaxSubmitLink.class);
    }

    @Test
    public void loadingPage_withSearchOrderWithoutOverrides_hidesShowExclusionStuff() {
        assertThat(searchOrderMock.getExcludedPaperIds()).isEmpty();

        getTester().startComponentInPage(makePanel());

        String b = "panel:form:showExcluded";
        getTester().assertInvisible(b);
        getTester().assertInvisible(b + "Label");
    }

    @Test
    public void loadingPage_withSearchOrderWithOverrides_showsShowExcludedStuff() {
        when(searchOrderMock.getExcludedPaperIds()).thenReturn(Collections.singletonList(3L));
        when(searchOrderMock.isShowExcluded()).thenReturn(false);

        getTester().startComponentInPage(makePanel());

        String b = "panel:form:showExcluded";
        getTester().assertComponent(b, AjaxCheckBox.class);
        getTester().assertLabel(b + "Label", "Show Exclusions");

        verify(searchOrderMock, times(3)).getExcludedPaperIds();
    }

    @Test
    public void changingSearchOrderSelection_addsTargetsAndSendsEvent() {
        getTester().startComponentInPage(makePanel());

        getTester().executeAjaxEvent(PANEL_ID + ":form:searchOrder", "change");

        String b = PANEL_ID + ":form:";
        getTester().assertComponentOnAjaxResponse(b + GLOBAL.getName());
        getTester().assertComponentOnAjaxResponse(b + NAME.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName() + "Label");

        // TODO how to assert the event was actually broadcast without issuing the test
        // info message
        getTester().assertInfoMessages("Sent SearchOrderChangeEvent");
    }

    @Test
    public void changingName_addsTargetsAndSendsEvent() {
        getTester().startComponentInPage(makePanel());

        getTester().executeAjaxEvent(PANEL_ID + ":form:name", "change");

        String b = PANEL_ID + ":form:";
        getTester().assertComponentOnAjaxResponse(b + GLOBAL.getName());
        getTester().assertComponentOnAjaxResponse(b + NAME.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName() + "Label");
    }

    @Test
    public void loadingPage_withSearchOrderWithCurrentOwner_rendersGlobalCheckBoxDisabled() {
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());
        getTester().assertEnabled(PANEL_ID + ":form:global");
    }

    @Test
    public void loadingPage_withSearchOrderWithDifferentOwner_rendersGlobalCheckBoxDisabled() {
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());
        getTester().assertDisabled(PANEL_ID + ":form:global");
    }

    @Test
    public void testSubmittingWithNewButton_createsNewSearchOrder() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form");

        formTester.submit("new");

        String b = PANEL_ID + ":form:";
        getTester().assertComponentOnAjaxResponse(b + GLOBAL.getName());
        getTester().assertComponentOnAjaxResponse(b + NAME.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getName() + "Label");

        verify(searchOrderMock, times(10)).getId();
        verify(searchOrderServiceMock, times(2)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(searchOrderServiceMock, never()).saveOrUpdate(searchOrderMock);
    }

    @Test
    public void testSubmittingWithDeleteButton_deletesSearchOrder() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        String b = PANEL_ID + ":form";
        FormTester formTester = getTester().newFormTester(b);
        formTester.submit("delete");

        b = b + ":";
        getTester().assertComponentOnAjaxResponse(b);

        verify(searchOrderMock, times(13)).getId();
        verify(searchOrderServiceMock, times(3)).findPageByFilter(isA(SearchOrderFilter.class),
            isA(PaginationContext.class));
        verify(searchOrderServiceMock).remove(searchOrderMock);
    }

    @Test
    public void withGlobalSearchOrders_withSameOwner_globalCheckBox_enabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().assertEnabled(PANEL_ID + ":form:global");

        verify(searchOrderMock, times(2)).getOwner();
    }

    @Test
    public void withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");

        verify(searchOrderMock, times(2)).getOwner();
    }

}
