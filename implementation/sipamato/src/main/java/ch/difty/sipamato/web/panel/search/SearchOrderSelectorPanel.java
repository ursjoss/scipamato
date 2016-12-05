package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.web.model.SearchOrderModel;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;

public class SearchOrderSelectorPanel extends AbstractPanel<SearchOrder> {
    private static final long serialVersionUID = 1L;

    private static final String CHANGE = "change";

    public SearchOrderSelectorPanel(String id, IModel<SearchOrder> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queueForm("form");
    }

    private void queueForm(String id) {
        queue(new Form<Void>(id));

        final String selectId = "searchOrder";
        queue(new Label(selectId + LABEL_TAG, new StringResourceModel(selectId + LABEL_RECOURCE_TAG, this, null)));

        final SearchOrderModel choices = new SearchOrderModel(1); // TODO use real user id
        final IChoiceRenderer<SearchOrder> choiceRenderer = new ChoiceRenderer<SearchOrder>(SearchOrder.DISPLAY_VALUE, SearchOrder.ID);
        final BootstrapSelect<SearchOrder> select = new BootstrapSelect<SearchOrder>("searchOrder", getModel(), choices, choiceRenderer);
        select.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
            }
        });
        queue(select);
    }

}
