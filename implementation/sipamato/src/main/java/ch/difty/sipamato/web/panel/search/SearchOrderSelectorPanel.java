package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.web.model.SearchOrderModel;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

/**
 * Panel offering the user the option of:
 *
 * <ul>
 * <li>selecting from previously saved search orders via a select box</li>
 * <li>changing the global flag of search orders (TODO)</li>
 * <li>saving new or modified search orders (TODO)</li>
 * </ul>
 *
 * Once a modification has been issued, the panel will issue a {@link SearchOrderChangeEvent} to the page.
 * The page and other panels within the page can then react to the new or modified selection.
 *
 * @author u.joss
 *
 */
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
        makeAndQueueSearchOrderSelectBox("searchOrder");
    }

    private void makeAndQueueSearchOrderSelectBox(final String id) {
        final SearchOrderModel choices = new SearchOrderModel(1); // TODO use real user id
        final IChoiceRenderer<SearchOrder> choiceRenderer = new ChoiceRenderer<SearchOrder>(SearchOrder.DISPLAY_VALUE, SearchOrder.ID);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withNoneSelectedText(noneSelectedModel.getObject()).withLiveSearch(true);
        final BootstrapSelect<SearchOrder> searchOrder = new BootstrapSelect<SearchOrder>(id, getModel(), choices, choiceRenderer).with(config);
        searchOrder.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
                info("Sent SearchOrderChangeEvent");
            }
        });
        searchOrder.add(new AttributeModifier("data-width", "fit"));
        queue(searchOrder);
    }

}
