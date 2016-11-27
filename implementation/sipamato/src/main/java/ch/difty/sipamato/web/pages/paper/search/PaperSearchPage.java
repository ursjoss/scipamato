package ch.difty.sipamato.web.pages.paper.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.ComplexPaperFilterProvider;
import ch.difty.sipamato.web.pages.paper.provider.ComplexSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

/**
 * The PaperSearchPage allows to add and combine individual search terms by instantiating the {@link PaperSearchCriteriaPage}
 * with the currently combined {@link CompositeComplexPaperFilter}. That page will instantiate a new instance of this page
 * after adding the new search term.
 *
 * @author u.joss
 */
@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<CompositeComplexPaperFilter> {

    private static final long serialVersionUID = 1L;

    private ComplexSortablePaperSlimProvider dataProvider;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(new CompositeComplexPaperFilter(null)));
    }

    public PaperSearchPage(IModel<CompositeComplexPaperFilter> filterModel) {
        super(filterModel);
    }

    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new ComplexSortablePaperSlimProvider(getModelObject());

        queueForm("form");
        queueResultPanel("resultPanel");
    }

    private void queueForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));
        queueResponsePageButton("addSearch", new PaperSearchCriteriaPage(getModelObject()));

        ComplexPaperFilterProvider p = new ComplexPaperFilterProvider(getModel());
        DataTable<ComplexPaperFilter, String> searchTerms = new BootstrapDefaultDataTable<ComplexPaperFilter, String>("searchTerms", makeTableColumns(), p, 10);
        searchTerms.setOutputMarkupId(true);
        searchTerms.add(new TableBehavior().striped().hover());
        queue(searchTerms);
    }

    private List<IColumn<ComplexPaperFilter, String>> makeTableColumns() {
        final List<IColumn<ComplexPaperFilter, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn("toString", null));
        return columns;
    }

    private PropertyColumn<ComplexPaperFilter, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<ComplexPaperFilter, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private void queueResultPanel(final String id) {
        queue(new ResultPanel(id, dataProvider));
    }

}
