package ch.difty.scipamato.core.web.paper.search;

import static ch.difty.scipamato.core.entity.search.SearchOrder.SearchOrderFields.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.entity.search.SearchOrder;

@SuppressWarnings("ResultOfMethodCallIgnored")
class SearchOrderSelectorPanelInEditModeTest extends SearchOrderSelectorPanelTest {

    @Override
    Mode getMode() {
        return Mode.EDIT;
    }

    @Test
    void loadingPage_withSearchOrderWithCurrentOwner_rendersGlobalCheckBoxDisabled() {
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());
        getTester().assertEnabled(PANEL_ID + ":form:global");
    }

    @Test
    void withGlobalSearchOrders_withSameOwner_globalCheckBox_enabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().assertEnabled(PANEL_ID + ":form:global");

        verify(searchOrderMock, times(3)).getOwner();
    }

    @Test
    void withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");

        verify(searchOrderMock, times(3)).getOwner();
    }

    @Test
    void changingName_forSearchOwnedByUser_addsTargetsAndSaves() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().executeAjaxEvent(PANEL_ID + ":form:name", "change");

        String b = PANEL_ID + ":form:";
        getTester().assertComponentOnAjaxResponse(b + GLOBAL.getFieldName());
        getTester().assertComponentOnAjaxResponse(b + NAME.getFieldName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getFieldName());
        getTester().assertComponentOnAjaxResponse(b + SHOW_EXCLUDED.getFieldName() + "Label");

        verify(searchOrderServiceMock).saveOrUpdate(isA(SearchOrder.class));
    }

    @Test
    void changingName_forSearchOwnedByDifferentUser_doesNotAddTargetNorSaves() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");
    }
}
