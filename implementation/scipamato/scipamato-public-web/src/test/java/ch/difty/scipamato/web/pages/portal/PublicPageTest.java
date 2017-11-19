package ch.difty.scipamato.web.pages.portal;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.persistence.PublicPaperService;
import ch.difty.scipamato.persistence.paging.PaginationContext;
import ch.difty.scipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PublicPageTest extends BasePageTest<PublicPage> {

    @MockBean
    private PublicPaperService serviceMock;

    private final List<PublicPaper> papers = new ArrayList<>();

    @Override
    protected void setUpHook() {
        super.setUpHook();

        papers.add(new PublicPaper(1l, 1l, 1000, "authors1", "title1", "location1", 2016, "goals1", "methods1", "population1", "result1", "comment1"));
        papers.add(new PublicPaper(2l, 2l, 1002, "authors2", "title2", "location2", 2017, "goals2", "methods2", "population2", "result2", "comment2"));

        when(serviceMock.countByFilter(isA(PublicPaperFilter.class))).thenReturn(papers.size());
        when(serviceMock.findPageByFilter(isA(PublicPaperFilter.class), isA(PaginationContext.class))).thenReturn(papers);
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
        getTester().assertComponent(b, Form.class);

        assertLabeledTextField(b, "number");
        assertLabeledTextField(b, "authorsSearch");
        assertLabeledTextField(b, "methodsSearch");
        assertLabeledTextField(b, "pubYearFrom");
        assertLabeledTextField(b, "pubYearUntil");
        assertLabeledCombo(b, "populationCodes");
        assertLabeledCombo(b, "studyDesignCodes");
    }

    private void assertLabeledCombo(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, BootstrapMultiSelect.class);
    }

    private void assertResultsTable(String b) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);

        assertTableRow(b + ":body:rows:1:cells", "authors1", "title1", "location1", "2016");
        assertTableRow(b + ":body:rows:2:cells", "authors2", "title2", "location2", "2017");
    }

    private void assertTableRow(String bb, String... values) {
        int i = 1;
        for (String v : values)
            getTester().assertLabel(bb + ":" + i++ + ":cell", v);
    }

}
