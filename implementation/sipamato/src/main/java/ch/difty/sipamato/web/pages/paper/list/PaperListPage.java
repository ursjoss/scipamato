package ch.difty.sipamato.web.pages.paper.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperSlimProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage<PaperSlim> {

    private static final long serialVersionUID = 1L;

    private PaperFilter filter;

    @SpringBean
    private PaperService paperService;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        filter = new PaperFilter();
    }

    protected void onInitialize() {
        super.onInitialize();

        final SortablePaperSlimProvider dataProvider = new SortablePaperSlimProvider(filter);

        queueFilterForm("searchForm", dataProvider);
        queueDataTable("table", dataProvider);
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(dataProvider, "filterState." + PaperFilter.AUTHOR_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(dataProvider, "filterState." + PaperFilter.METHODS_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("fieldSearch", PropertyModel.of(dataProvider, "filterState." + PaperFilter.SEARCH_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(dataProvider, "filterState." + PaperFilter.PUB_YEAR_FROM)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(dataProvider, "filterState." + PaperFilter.PUB_YEAR_UNTIL)), Optional.empty());
    }

    private void queueFilterForm(final String id, final SortablePaperSlimProvider dataProvider) {
        FilterForm<PaperFilter> form = new FilterForm<PaperFilter>(id, dataProvider);
        queue(form);
    }

    private void queueDataTable(final String id, final SortablePaperSlimProvider dataProvider) {
        final DataTable<PaperSlim, String> table = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, 20);
        table.setOutputMarkupId(true);
        table.add(new TableBehavior().striped().hover());
        queue(table);
    }

    private List<IColumn<PaperSlim, String>> makeTableColumns() {
        final List<IColumn<PaperSlim, String>> columns = new ArrayList<>();
        // TODO get rid of db stuff define table fields somewhere else
        columns.add(makePropertyColumn(Paper.ID, ch.difty.sipamato.db.tables.Paper.PAPER.ID.getName()));
        columns.add(makePropertyColumn(Paper.FIRST_AUTHOR, ch.difty.sipamato.db.tables.Paper.PAPER.FIRST_AUTHOR.getName()));
        columns.add(makePropertyColumn(Paper.PUBL_YEAR, ch.difty.sipamato.db.tables.Paper.PAPER.PUBLICATION_YEAR.getName()));
        columns.add(makeClickableColumn(Paper.TITLE, ch.difty.sipamato.db.tables.Paper.PAPER.TITLE.getName(),
                (IModel<PaperSlim> m) -> setResponsePage(new PaperEntryPage(Model.of(paperService.findById(m.getObject().getId()).orElse(new Paper()))))));
        return columns;
    }

    private PropertyColumn<PaperSlim, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private ClickablePropertyColumn<PaperSlim, String> makeClickableColumn(String propExpression, String sortProperty, SerializableConsumer<IModel<PaperSlim>> consumer) {
        return new ClickablePropertyColumn<PaperSlim, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression, consumer);
    }
}
