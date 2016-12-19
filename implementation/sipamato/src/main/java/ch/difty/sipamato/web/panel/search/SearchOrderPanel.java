package ch.difty.sipamato.web.panel.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.component.SerializableBiConsumer;
import ch.difty.sipamato.web.component.SerializableBiFunction;
import ch.difty.sipamato.web.component.SerializableConsumer;
import ch.difty.sipamato.web.component.SerializableSupplier;
import ch.difty.sipamato.web.component.data.LinkIconColumn;
import ch.difty.sipamato.web.component.table.column.ClickablePropertyColumn2;
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

    private DataTable<SearchCondition, String> searchConditions;

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
        queueNewButton("addSearchCondition", (scModel, soId) -> new PaperSearchCriteriaPage(scModel, soId), () -> Model.of(new SearchCondition()));

        SearchConditionProvider p = new SearchConditionProvider(getModel());
        searchConditions = new BootstrapDefaultDataTable<SearchCondition, String>("searchConditions", makeTableColumns(), p, 10);
        searchConditions.setOutputMarkupId(true);
        searchConditions.add(new TableBehavior().striped().hover());
        queue(searchConditions);
    }

    private void queueNewButton(String id, SerializableBiFunction<IModel<SearchCondition>, Long, BasePage<SearchCondition>> pageFunction, SerializableSupplier<IModel<SearchCondition>> modelProvider) {
        queue(new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null), Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderPanel.this.isSearchOrderIdDefined());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                setResponsePage(pageFunction.apply(modelProvider.get(), SearchOrderPanel.this.getModelObject().getId()));
            }
        });
    }

    protected boolean isSearchOrderIdDefined() {
        return getModelObject() != null && getModelObject().getId() != null;
    }

    private List<IColumn<SearchCondition, String>> makeTableColumns() {
        final List<IColumn<SearchCondition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("displayValue", null, (IModel<SearchCondition> m, Long soId) -> setResponsePage(new PaperSearchCriteriaPage(m, soId)), () -> getModelObject().getId()));
        columns.add(makeLinkIconColumn("remove", (IModel<SearchCondition> m) -> getModelObject().remove(m.getObject())));
        return columns;
    }

    private ClickablePropertyColumn2<SearchCondition, String, Long> makeClickableColumn(String propExpression, String sortProperty, SerializableBiConsumer<IModel<SearchCondition>, Long> consumer,
            SerializableSupplier<Long> supplier) {
        final StringResourceModel displayModel = new StringResourceModel("column.header." + propExpression, this, null);
        return new ClickablePropertyColumn2<SearchCondition, String, Long>(displayModel, sortProperty, propExpression, consumer, supplier);
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
                target.add(searchConditions);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withDroppedConditionId(rowModel.getObject().getSearchConditionId()));
                info("Removed " + rowModel.getObject().getDisplayValue());
            }
        };
    }

}
