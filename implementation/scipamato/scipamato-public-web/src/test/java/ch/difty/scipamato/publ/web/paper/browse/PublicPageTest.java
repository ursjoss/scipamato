package ch.difty.scipamato.publ.web.paper.browse;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
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
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
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

        papers.add(new PublicPaper(1l, 1l, 1000, "authors1", "title1", "location1", 2016, "goals1", "methods1",
                "population1", "result1", "comment1"));
        papers.add(new PublicPaper(2l, 2l, 1002, "authors2", "title2", "location2", 2017, "goals2", "methods2",
                "population2", "result2", "comment2"));

        when(serviceMock.countByFilter(isA(PublicPaperFilter.class))).thenReturn(papers.size());
        when(serviceMock.findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class)))
            .thenReturn(papers);

    }

    @Override
    protected void doVerify() {
        verify(serviceMock).countByFilter(isA(PublicPaperFilter.class));
        verify(serviceMock).findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class));
        // used in navigateable
        verify(serviceMock, times(1)).findPageOfNumbersByFilter(isA(PublicPaperFilter.class),
            isA(PaginationContext.class));

        verify(codeClassServiceMock).find("en_us");
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
        assertSearchForm("searchForm");
        assertResultsTable("results");
    }

    private void assertSearchForm(String b) {
        getTester().newFormTester(b)
            .submit("query");
        getTester().assertComponent(b, Form.class);

        String bb = b + ":tabs";
        getTester().assertComponent(bb, ClientSideBootstrapTabbedPanel.class);
        bb += ":panelsContainer:panels:";

        String bbb = bb + "1:tab1Form";
        getTester().assertComponent(bbb, Form.class);
        getTester().assertComponent(bbb + ":simpleFilterPanel", SimpleFilterPanel.class);

        bbb = bb + "3:tab2Form";
        int i = 1;
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);
        assertCodeClass(bbb, i++);

        getTester().assertComponent(b + ":query", BootstrapButton.class);
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

        verify(codeClassServiceMock).find("en_us");
    }
}
