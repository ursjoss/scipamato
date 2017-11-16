package ch.difty.scipamato.web.pages.portal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.provider.PublicPaperProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("/")
@WicketHomePage
public class PublicPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";

    private static final int RESULT_PAGE_SIZE = 20;

    private PublicPaperFilter filter;
    private PublicPaperProvider dataProvider;
    private DataTable<PublicPaper, String> results;

    public PublicPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new PublicPaperFilter();
        dataProvider = new PublicPaperProvider(filter, RESULT_PAGE_SIZE);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("searchForm");

        makeAndQueueResultTable("results");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<PublicPaperFilter>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                System.out.println("whooo - submitted");
            }
        });

        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, PublicPaperFilter.METHODS_MASK)));
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, PublicPaperFilter.AUTHOR_MASK)));
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_FROM)));
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_UNTIL)));
        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, PublicPaperFilter.NUMBER)));
    }

    private void makeAndQueueResultTable(String id) {
        results = new BootstrapDefaultDataTable<PublicPaper, String>(id, makeTableColumns(), dataProvider, dataProvider.getRowsPerPage());
        results.setOutputMarkupId(true);
        results.add(new TableBehavior().striped().hover());
        queue(results);
    }

    private List<IColumn<PublicPaper, String>> makeTableColumns() {
        final List<IColumn<PublicPaper, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(PublicPaper.AUTHORS));
        columns.add(makePropertyColumn(PublicPaper.TITLE));
        columns.add(makePropertyColumn(PublicPaper.LOCATION));
        columns.add(makePropertyColumn(PublicPaper.PUBL_YEAR));
        return columns;
    }

    private PropertyColumn<PublicPaper, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression);
    }
}
