package ch.difty.scipamato.publ.web.paper.browse;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.persistence.api.CodeClassService;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;
import ch.difty.scipamato.publ.web.common.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.BootstrapTabbedPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PublicPageTest extends BasePageTest<PublicPage> {

    @MockBean
    private PublicPaperService serviceMock;

    @MockBean
    private CodeClassService codeClassServiceMock;

    private final List<PublicPaper> papers = new ArrayList<>();

    @Override
    protected void setUpHook() {
        super.setUpHook();

        papers.add(new PublicPaper(1l, 10l, 1000, "authors1", "title1", "location1", 2016, "goals1", "methods1",
                "population1", "result1", "comment1"));
        papers.add(new PublicPaper(2l, 20l, 1002, "authors2", "title2", "location2", 2017, "goals2", "methods2",
                "population2", "result2", "comment2"));

        when(serviceMock.countByFilter(isA(PublicPaperFilter.class))).thenReturn(papers.size());
        when(serviceMock.findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class)))
            .thenReturn(papers);
    }

    @Override
    protected void doVerify() {
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(serviceMock, codeClassServiceMock);
    }

    @Override
    protected PublicPage makePage() {
        return new PublicPage(new PageParameters());
    }

    @Override
    protected Class<PublicPage> getPageClass() {
        return PublicPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "searchForm";
        getTester().assertComponent(b, Form.class);

        assertTabPanelWithFirstTabVisible(b + ":tabs");

        getTester().assertComponent(b + ":query", BootstrapButton.class);
        getTester().assertComponent(b + ":clear", BootstrapButton.class);

        // query was not yet executed and results panel is still invisible
        getTester().assertInvisible("results");
    }

    private void assertTabPanelWithFirstTabVisible(String b) {
        getTester().assertComponent(b, BootstrapTabbedPanel.class);

        // both tab titles are visible
        assertTabTitle(b, 0, "Simple Search");
        assertTabTitle(b, 1, "Extended Search");

        // first tab is visible
        b += ":panel:tab1Form";
        getTester().assertComponent(b, Form.class);
        getTester().assertComponent(b + ":simpleFilterPanel", SimpleFilterPanel.class);
    }

    private void assertTabTitle(String b, int index, String title) {
        String bb = b + ":tabs-container:tabs:" + index + ":link";
        getTester().assertComponent(bb, Link.class);
        getTester().assertLabel(bb + ":title", title);
    }

    @Test
    public void clickingQuery_showsResultPanel() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        // trigger the roundtrip to get the data by clicking 'query'
        // this should make the result panel visible
        getTester().newFormTester("searchForm")
            .submit("query");

        String b = "searchForm";
        getTester().assertComponent(b, Form.class);

        assertTabPanelWithFirstTabVisible(b + ":tabs");

        getTester().assertComponent(b + ":query", BootstrapButton.class);

        assertResultsTable("results");

        verify(serviceMock).countByFilter(isA(PublicPaperFilter.class));
        verify(serviceMock).findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class));
        // used in navigateable
        verify(serviceMock, times(1)).findPageOfNumbersByFilter(isA(PublicPaperFilter.class),
            isA(PaginationContext.class));

    }

    private void assertResultsTable(String b) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);

        assertTableRow(b + ":body:rows:1:cells", "authors1", "title1", "location1", "2016");
        assertTableRow(b + ":body:rows:2:cells", "authors2", "title2", "location2", "2017");
    }

    private void assertTableRow(final String bb, final String... values) {
        int i = 1;
        for (final String v : values) {
            if (i != 2)
                getTester().assertLabel(bb + ":" + i++ + ":cell", v);
            else
                getTester().assertLabel(bb + ":" + i++ + ":cell:link:label", v);
        }
    }

    @Test
    public void clickingTab2Title_showsTab2() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        String b = "searchForm";
        String bb = b + ":tabs";
        // Switch to the second tab
        getTester().clickLink(bb + ":tabs-container:tabs:1:link");

        assertTabPanelWithSecondTabVisible(bb);

        verify(codeClassServiceMock).find("en_us");
    }

    private void assertTabPanelWithSecondTabVisible(String b) {
        getTester().assertComponent(b, BootstrapTabbedPanel.class);

        // both tab titles are visible
        assertTabTitle(b, 0, "Simple Search");
        assertTabTitle(b, 1, "Extended Search");

        // second tab is visible
        String bb = b + ":panel:tab2Form";
        getTester().assertComponent(bb, Form.class);
        getTester().assertComponent(bb + ":simpleFilterPanel", SimpleFilterPanel.class);

        int i = 1;
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
        assertCodeClass(bb, i++);
    }

    private void assertCodeClass(final String esc, final int ccId) {
        final String compId = esc + ":codesOfClass" + ccId;
        getTester().assertLabel(compId + "Label", "CC" + ccId);
        getTester().assertComponent(compId, BootstrapMultiSelect.class);
    }

    @Test
    public void clickingTitle_forwardsToDetailsPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());
        getTester().newFormTester("searchForm")
            .submit("query");

        getTester().clickLink("results:body:rows:1:cells:2:cell:link");
        getTester().assertRenderedPage(PublicPaperDetailPage.class);

        verify(serviceMock, times(1)).countByFilter(isA(PublicPaperFilter.class));
        verify(serviceMock, times(1)).findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class));
        // used in navigateable
        verify(serviceMock, times(1)).findPageOfNumbersByFilter(isA(PublicPaperFilter.class),
            isA(PaginationContext.class));
    }

}
