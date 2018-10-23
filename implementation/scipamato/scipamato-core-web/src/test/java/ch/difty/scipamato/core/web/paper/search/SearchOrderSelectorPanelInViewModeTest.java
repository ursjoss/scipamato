package ch.difty.scipamato.core.web.paper.search;

import static org.mockito.Mockito.when;

import org.junit.Test;

import ch.difty.scipamato.common.web.Mode;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SearchOrderSelectorPanelInViewModeTest extends SearchOrderSelectorPanelTest {

    @Override
    Mode getMode() {
        return Mode.VIEW;
    }

    @Test
    public void withGlobalSearchOrders_withSameOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");
    }

    @Test
    public void withGlobalSearchOrders_withOtherOwner_globalCheckBox_disabled() {
        when(searchOrderMock.getName()).thenReturn(VALID_NAME);
        when(searchOrderMock.getOwner()).thenReturn(OWNER_ID + 1);
        getTester().startComponentInPage(makePanel());

        getTester().assertDisabled(PANEL_ID + ":form:global");
    }

}
