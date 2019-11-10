package ch.difty.scipamato.core.web.paper.search;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.search.SearchOrder;

class SearchOrderSelectorPanelInViewModeTest extends SearchOrderSelectorPanelTest {

    @Override
    Mode getMode() {
        return Mode.VIEW;
    }

    @Test
    void withGlobalSearchOrders_withSameOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");
    }

    @Test
    void withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");
    }

    @Test
    void changingName_forGlobalNotSelfOwnedSearch_disablesName() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.isGlobal()).thenReturn(true);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        assertNameDisabled();
    }

    private void assertNameDisabled() {
        getTester().startComponentInPage(makePanel());
        getTester().assertDisabled(PANEL_ID + ":form:name");
    }

    @Test
    void changingName_forGlobalSelfOwnedSearch_disablesName() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.isGlobal()).thenReturn(true);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        assertNameDisabled();
    }

    @Test
    void changingName_forNotGlobalNotSelfOwnedSearch_disablesName() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.isGlobal()).thenReturn(false);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        assertNameDisabled();
    }

    @Test
    void changingName_forNotGlobalSelfOwnedSearch_doesAddTargetAndSaves() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.isGlobal()).thenReturn(false);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().executeAjaxEvent(PANEL_ID + ":form:name", "change");

        verify(searchOrderServiceMock).saveOrUpdate(isA(SearchOrder.class));
    }
}
