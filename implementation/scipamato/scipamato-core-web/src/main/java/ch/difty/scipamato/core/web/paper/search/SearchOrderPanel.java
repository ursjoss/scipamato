package ch.difty.scipamato.core.web.paper.search;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.component.SerializableBiConsumer;
import ch.difty.scipamato.common.web.component.SerializableBiFunction;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.SerializableSupplier;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn2;
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.web.common.BasePanel;
import ch.difty.scipamato.core.web.paper.PageFactory;
import ch.difty.scipamato.core.web.paper.SearchConditionProvider;
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent;

@SuppressWarnings("SameParameterValue")
public class SearchOrderPanel extends BasePanel<SearchOrder> {

    private static final long serialVersionUID = 1L;

    private DataTable<SearchCondition, String> searchConditions;

    @SpringBean
    private PageFactory pageFactory;

    SearchOrderPanel(String id, IModel<SearchOrder> model, Mode mode) {
        super(id, model, mode);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queueForm("form");
    }

    private void queueForm(final String id) {
        queue(new Form<>(id));
        queueNewButton("addSearchCondition", pageFactory.newPaperSearchCriteriaPage(),
            () -> Model.of(new SearchCondition()));

        SearchConditionProvider p = new SearchConditionProvider(
            new PropertyModel<>(getModel(), SearchOrder.SearchOrderFields.CONDITIONS.getName()));
        searchConditions = new BootstrapDefaultDataTable<>("searchConditions", makeTableColumns(), p, 10);
        searchConditions.setOutputMarkupId(true);
        searchConditions.add(new TableBehavior()
            .striped()
            .hover());
        queue(searchConditions);
    }

    private void queueNewButton(String id,
        SerializableBiFunction<IModel<SearchCondition>, Long, GenericWebPage<SearchCondition>> pageFunction,
        SerializableSupplier<IModel<SearchCondition>> modelProvider) {
        queue(new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null), Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderPanel.this.isSearchOrderIdDefined() && isEntitledToModify(
                    SearchOrderPanel.this.getModelObject()));
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                setResponsePage(pageFunction.apply(modelProvider.get(), SearchOrderPanel.this
                    .getModelObject()
                    .getId()));
            }
        });
    }

    boolean isSearchOrderIdDefined() {
        return getModelObject() != null && getModelObject().getId() != null;
    }

    private List<IColumn<SearchCondition, String>> makeTableColumns() {
        final List<IColumn<SearchCondition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("displayValue", null,
            pageFactory.setResponsePageToPaperSearchCriteriaPageConsumer(this), () -> getModelObject().getId()));
        columns.add(
            makeLinkIconColumn("remove", (IModel<SearchCondition> m) -> getModelObject().remove(m.getObject())));
        return columns;
    }

    private boolean isEntitledToModify(final SearchOrder searchOrder) {
        return searchOrder.getOwner() == getActiveUser().getId() || !searchOrder.isGlobal();
    }

    private ClickablePropertyColumn2<SearchCondition, String, Long> makeClickableColumn(String propExpression,
        String sortProperty, SerializableBiConsumer<IModel<SearchCondition>, Long> consumer,
        SerializableSupplier<Long> supplier) {
        final StringResourceModel displayModel = new StringResourceModel("column.header." + propExpression, this, null);
        return new ClickablePropertyColumn2<>(displayModel, sortProperty, propExpression, consumer, supplier);
    }

    private IColumn<SearchCondition, String> makeLinkIconColumn(String id,
        SerializableConsumer<IModel<SearchCondition>> consumer) {
        return new LinkIconColumn<>(new StringResourceModel("column.header." + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean shouldBeVisible() {
                return isEntitledToModify(getModelObject());
            }

            @Override
            protected IModel<String> createIconModel(IModel<SearchCondition> rowModel) {
                return Model.of("fa fa-fw fa-trash-o text-danger");
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<SearchCondition> rowModel,
                AjaxLink<Void> link) {
                if (isEntitledToModify(getModelObject())) {
                    consumer.accept(rowModel);
                    target.add(searchConditions);
                    send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).withDroppedConditionId(
                        rowModel
                            .getObject()
                            .getSearchConditionId()));
                    info("Removed " + rowModel
                        .getObject()
                        .getDisplayValue());
                }
            }
        };
    }

}
