package ch.difty.sipamato.web.panel.search;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.persistance.jooq.search.SearchOrderFilter;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.panel.PanelTest;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;

public class SearchOrderSelectorPanelTest extends PanelTest<SearchOrderSelectorPanel> {

    private final long ID = 17l;

    @MockBean
    private SearchOrderService searchOrderServiceMock;

    @Mock
    private SearchOrder searchOrderMock, searchOrderMock2;

    private final List<SearchOrder> searchOrders = new ArrayList<>();
    private final List<SearchCondition> searchConditions = new ArrayList<>();

    @Override
    protected SearchOrderSelectorPanel makePanel() {
        return new SearchOrderSelectorPanel(PANEL_ID, Model.of(searchOrderMock));
    }

    @Override
    protected void setUpHook() {
        super.setUpHook();

        searchOrders.add(searchOrderMock);
        searchOrders.add(new SearchOrder(20l, 2, true, searchConditions, null));
        when(searchOrderServiceMock.findByFilter(isA(SearchOrderFilter.class), isA(Pageable.class))).thenReturn(searchOrders);
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

        String bb = b + ":searchOrder";
        getTester().assertComponent(bb, BootstrapSelect.class);

        bb = b + ":submit";
        getTester().assertComponent(bb, SubmitLink.class);
    }

    @Test
    public void testSelectUpdateBehavior() {
        getTester().startComponentInPage(makePanel());

        getTester().executeAjaxEvent(PANEL_ID + ":form:searchOrder", "change");

        // TODO how to assert the event was actually broadcast without issuing the test info message
        getTester().assertInfoMessages("Sent SearchOrderChangeEvent");

        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":form:submit");
    }

    @Test
    public void testSubmitting_saveSearchOrder() {
        getTester().startComponentInPage(makePanel());
        FormTester formTester = getTester().newFormTester(PANEL_ID + ":form");
        formTester.submit("submit");

        verify(searchOrderMock, times(8)).getId();
        verify(searchOrderServiceMock, times(3)).findByFilter(isA(SearchOrderFilter.class), isA(Pageable.class));
        verify(searchOrderServiceMock).saveOrUpdate(searchOrderMock);
    }

}
