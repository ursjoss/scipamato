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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private FilterForm<PaperFilter> form;

    private PaperFilter filter;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();

        initDefaultModel();

        final SortablePaperProvider dataProvider = new SortablePaperProvider(filter);
        form = new FilterForm<PaperFilter>("searchForm", dataProvider);
        add(form);

        final List<IColumn<Paper, String>> columns = new ArrayList<>();
        columns.add(new PropertyColumn<Paper, String>(new StringResourceModel("column.header.id", this, null), Paper.ID, Paper.ID));
        columns.add(new PropertyColumn<Paper, String>(new StringResourceModel("column.header.publicationYear", this, null), Paper.PUBL_YEAR, Paper.PUBL_YEAR));
        columns.add(new PropertyColumn<Paper, String>(new StringResourceModel("column.header.firstAuthor", this, null), Paper.FIRST_AUTHOR, Paper.FIRST_AUTHOR));
        columns.add(new PropertyColumn<Paper, String>(new StringResourceModel("column.header.title", this, null), Paper.TITLE, Paper.TITLE));
        DataTable<Paper, String> table = new BootstrapDefaultDataTable<>("table", columns, dataProvider, 20);
        table.setOutputMarkupId(true);
        table.add(new TableBehavior().striped().hover());
        form.add(table);

        addFieldAndLabel(new TextField<String>("searchField", PropertyModel.of(dataProvider, "filterState." + PaperFilter.SEARCH_MASK)), Optional.empty());
    }

    private void initDefaultModel() {
        filter = new PaperFilter();
    }

}
