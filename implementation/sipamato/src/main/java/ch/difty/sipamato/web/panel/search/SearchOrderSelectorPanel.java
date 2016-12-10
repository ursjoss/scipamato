package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.model.SearchOrderModel;
import ch.difty.sipamato.web.pages.Mode;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

/**
 * Panel offering the user the option of:
 *
 * <ul>
 * <li>selecting from previously saved search orders via a select box</li>
 * <li>changing the global flag of search orders (TODO)</li>
 * <li>saving new or modified search orders</li>
 * </ul>
 *
 * Once a modification has been issued, the panel will issue a {@link SearchOrderChangeEvent} to the page.
 * The page and other panels within the page can then react to the new or modified selection.
 *
 * @author u.joss
 */
public class SearchOrderSelectorPanel extends AbstractPanel<SearchOrder> {
    private static final long serialVersionUID = 1L;

    private static final String CHANGE = "change";

    @SpringBean
    private SearchOrderService searchOrderService;

    private Form<SearchOrder> form;
    private SubmitLink submitLink;

    public SearchOrderSelectorPanel(String id, IModel<SearchOrder> model) {
        super(id, model, Mode.EDIT);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(String id) {
        form = new Form<SearchOrder>(id, getModel());
        queue(form);
        makeAndQueueSearchOrderSelectBox("searchOrder");
        makeAndQueueSubmitButton("submit");
    }

    private void makeAndQueueSearchOrderSelectBox(final String id) {
        final SearchOrderModel choices = new SearchOrderModel(getActiveUser().getId());
        final IChoiceRenderer<SearchOrder> choiceRenderer = new ChoiceRenderer<SearchOrder>(SearchOrder.DISPLAY_VALUE, SearchOrder.ID);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withNoneSelectedText(noneSelectedModel.getObject()).withLiveSearch(true);
        final BootstrapSelect<SearchOrder> searchOrder = new BootstrapSelect<SearchOrder>(id, getModel(), choices, choiceRenderer).with(config);
        searchOrder.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(submitLink);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
                info("Sent SearchOrderChangeEvent");
            }
        });
        searchOrder.add(new AttributeModifier("data-width", "fit"));
        queue(searchOrder);
    }

    private void makeAndQueueSubmitButton(String id) {
        submitLink = new SubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isModelSelected());
            }

            @Override
            public void onSubmit() {
                super.onSubmit();
                if (getModelObject() != null) {
                    searchOrderService.saveOrUpdate(getModelObject());
                }
            }
        };
        submitLink.add(new ButtonBehavior());
        submitLink.setBody(new StringResourceModel(getSubmitLinkResourceLabel()));
        submitLink.setDefaultFormProcessing(false);
        submitLink.setEnabled(!isViewMode());
        submitLink.setOutputMarkupId(true);
        queue(submitLink);
    }

    protected boolean isModelSelected() {
        return getModelObject() != null && getModelObject().getId() != null;
    }

}
