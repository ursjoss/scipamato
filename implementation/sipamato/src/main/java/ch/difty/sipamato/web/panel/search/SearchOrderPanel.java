package ch.difty.sipamato.web.panel.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.SerializableFunction;
import ch.difty.sipamato.web.component.SerializableSupplier;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.SearchConditionProvider;
import ch.difty.sipamato.web.pages.paper.search.PaperSearchCriteriaPage;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

public class SearchOrderPanel extends AbstractPanel<SearchOrder> {

    private static final long serialVersionUID = 1L;

    private DataTable<SearchCondition, String> searchTerms;

    public SearchOrderPanel(String id, IModel<SearchOrder> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queueForm("form");
    }

    private void queueForm(final String id) {
        queue(new Form<>(id));
        queueNewButton("addSearch", (IModel<SearchOrder> fm) -> new PaperSearchCriteriaPage(fm), () -> getModel());

        SearchConditionProvider p = new SearchConditionProvider(getModel());
        searchTerms = new BootstrapDefaultDataTable<SearchCondition, String>("searchTerms", makeTableColumns(), p, 10);
        searchTerms.setOutputMarkupId(true);
        searchTerms.add(new TableBehavior().striped().hover());
        queue(searchTerms);
    }

    private void queueNewButton(String id, SerializableFunction<IModel<SearchOrder>, BasePage<SearchCondition>> pageFunction, SerializableSupplier<IModel<SearchOrder>> modelProvider) {
        queue(new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null), Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                setResponsePage(pageFunction.apply(modelProvider.get()));
            }
        });
    }

    private List<IColumn<SearchCondition, String>> makeTableColumns() {
        final List<IColumn<SearchCondition, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn("displayValue", null));
        columns.add(makeLinkIconColumn("remove", (IModel<SearchCondition> m) -> getModelObject().remove(m.getObject())));
        return columns;
    }

    private IColumn<SearchCondition, String> makeLinkIconColumn(String id, SerializableConsumer<IModel<SearchCondition>> consumer) {
        return new LinkIconColumn<SearchCondition>(new StringResourceModel("column.header." + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(IModel<SearchCondition> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<SearchCondition> rowModel, AjaxLink<Void> link) {
                consumer.accept(rowModel);
                target.add(searchTerms);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
            }
        };
    }

    private PropertyColumn<SearchCondition, String> makePropertyColumn(String propExpression, String sortProperty) {
        return new PropertyColumn<SearchCondition, String>(new StringResourceModel("column.header." + propExpression, this, null), sortProperty, propExpression);
    }

}
