package ch.difty.scipamato.core.web.paper.search;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.web.Mode;

class SearchOrderPanelInEditModeTest extends SearchOrderPanelTest {

    @Override
    Mode getMode() {
        return Mode.EDIT;
    }

    @Test
    void clickingDeleteIconLink() {
        getTester().startComponentInPage(makePanel());
        getTester().assertContains("foo");
        getTester().clickLink("panel:form:searchConditions:body:rows:1:cells:2:cell:link");
        getTester().assertInfoMessages("Removed foo");
        getTester().assertComponentOnAjaxResponse(PANEL_ID + ":form:searchConditions");
    }
}
