package ch.difty.sipamato.web.pages.paper.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.SerializableFunction;
import ch.difty.sipamato.web.component.SerializableSupplier;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.ComplexPaperFilterProvider;
import ch.difty.sipamato.web.pages.paper.provider.ComplexSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;
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

    DataTable<ComplexPaperFilter, String> searchTerms;

    ResultPanel resultPanel;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(new CompositeComplexPaperFilter(null)));
    }

    public PaperSearchPage(IModel<CompositeComplexPaperFilter> filterModel) {
        super(filterModel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new ComplexSortablePaperSlimProvider(getModelObject());

        queueForm("form");
        queueResultPanel("resultPanel");
    }

    private void queueForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));
        queueNewButton("addSearch", (IModel<CompositeComplexPaperFilter> fm) -> new PaperSearchCriteriaPage(fm), () -> getModel());

        ComplexPaperFilterProvider p = new ComplexPaperFilterProvider(getModel());
        searchTerms = new BootstrapDefaultDataTable<ComplexPaperFilter, String>("searchTerms", makeTableColumns(), p, 10);
        searchTerms.setOutputMarkupId(true);
        searchTerms.add(new TableBehavior().striped().hover());
        queue(searchTerms);
    }

    private void queueNewButton(String id, SerializableFunction<IModel<CompositeComplexPaperFilter>, BasePage<ComplexPaperFilter>> pageFunction,
            SerializableSupplier<IModel<CompositeComplexPaperFilter>> modelProvider) {
        queue(new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null), Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                setResponsePage(pageFunction.apply(modelProvider.get()));
            }
        });
    }

    private List<IColumn<ComplexPaperFilter, String>> makeTableColumns() {
        final List<IColumn<ComplexPaperFilter, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn("toString", null));
        columns.add(makeLinkIconColumn("remove", (IModel<ComplexPaperFilter> m) -> getModelObject().remove(m.getObject())));
        return columns;
    }

    private IColumn<ComplexPaperFilter, String> makeLinkIconColumn(String id, SerializableConsumer<IModel<ComplexPaperFilter>> consumer) {
        return new LinkIconColumn<ComplexPaperFilter>(new StringResourceModel("column.header." + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<ComplexPaperFilter> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<ComplexPaperFilter> rowModel) {
                return new StringResourceModel("column.title.remove", PaperSearchPage.this, null);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<ComplexPaperFilter> rowModel, AjaxLink link) {
                consumer.accept(rowModel);
                target.add(searchTerms);
                target.add(resultPanel);
            }
        };
    }

    private PropertyColumn<ComplexPaperFilter, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<ComplexPaperFilter, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

    private void queueResultPanel(final String id) {
        resultPanel = new ResultPanel(id, dataProvider);
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

}
