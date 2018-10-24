package ch.difty.scipamato.core.web.paper.search;

import org.junit.Test;

import ch.difty.scipamato.common.web.Mode;

public class SearchOrderPanelInEditModeTest extends SearchOrderPanelTest {

    @Override
    Mode getMode() {
        return Mode.EDIT;
    }

    @Test
    public void clickingDeleteIconLink() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContains("foo");
        getTester().clickLink("panel:form:searchConditions:body:rows:1:cells:2:cell:link");
        getTester().assertInfoMessages("Removed foo");
        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":form:searchConditions");
    }
}
