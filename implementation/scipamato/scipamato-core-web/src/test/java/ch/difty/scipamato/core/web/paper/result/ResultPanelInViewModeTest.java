package ch.difty.scipamato.core.web.paper.result;

import org.apache.wicket.ajax.markup.html.AjaxLink;

import ch.difty.scipamato.common.web.Mode;

class ResultPanelInViewModeTest extends ResultPanelTest {

    @Override
    protected Mode getMode() {
        return Mode.VIEW;
    }

    @Override
    protected void assertTableRow(String bb) {
        getTester().assertLabel(bb + ":1:cell", "1");
        getTester().assertLabel(bb + ":2:cell", String.valueOf(NUMBER));
        getTester().assertLabel(bb + ":3:cell", "firstAuthor");
        getTester().assertLabel(bb + ":4:cell", "2016");
        getTester().assertLabel(bb + ":5:cell:link:label", "title");
        getTester().assertContainsNot(bb + ":6:cell:link:image");
        getTester().assertContainsNot(bb + ":7:cell:link:image");
    }

}
