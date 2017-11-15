package ch.difty.scipamato.web.pages.portal;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class PublicPageTest extends BasePageTest<PublicPage> {

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
    }

    private void assertResultsTable(String b) {
        getTester().debugComponentTrees();
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
