package ch.difty.sipamato.web.pages.paper.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.PaperFilter;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.SortablePaperProvider;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private FilterForm<PaperFilter> form;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();

        List<IColumn<Paper, String>> columns = new ArrayList<>();
        // TODO property model for labels
        columns.add(new PropertyColumn<Paper, String>(new Model<>("ID"), Paper.ID));
        columns.add(new PropertyColumn<Paper, String>(new Model<>("Year"), Paper.PUBL_YEAR));
        columns.add(new PropertyColumn<Paper, String>(new Model<>("Fist Author"), Paper.FIRST_AUTHOR));
        columns.add(new PropertyColumn<Paper, String>(new Model<>("Titel"), Paper.TITLE));

        SortablePaperProvider dataProvider = new SortablePaperProvider();
        columns = new ArrayList<>(columns);

        form = new FilterForm<PaperFilter>("searchForm", dataProvider);
        add(form);

        DataTable<Paper, String> table = new BootstrapDefaultDataTable<>("table", columns, dataProvider, 20);
        table.setOutputMarkupId(true);
        form.add(table);

        addFieldAndLabel(new TextField<String>("searchField", PropertyModel.of(dataProvider, "filterState." + PaperFilter.SEARCH_MASK)));
    }

    private void addFieldAndLabel(FormComponent<?> field) {
        addFieldAndLabel(form, field, Optional.empty());
    }

}
