package ch.difty.scipamato.core.web.paper.search;

import static org.mockito.Mockito.*;

import org.junit.Test;

import ch.difty.scipamato.common.web.Mode;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SearchOrderSelectorPanelInEditModeTest extends SearchOrderSelectorPanelTest {

    @Override
    Mode getMode() {
        return Mode.EDIT;
    }

    @Test
    public void loadingPage_withSearchOrderWithCurrentOwner_rendersGlobalCheckBoxDisabled() {
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());
        getTester().assertEnabled(PANEL_ID + ":form:global");
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
