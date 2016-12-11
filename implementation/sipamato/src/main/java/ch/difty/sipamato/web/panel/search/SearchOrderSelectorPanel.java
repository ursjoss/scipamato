package ch.difty.sipamato.web.panel.search;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.model.SearchOrderModel;
import ch.difty.sipamato.web.pages.Mode;
import ch.difty.sipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

/**
 * Panel offering the user the option of:
 *
 * <ul>
 * <li>selecting from previously saved search orders via a select box</li>
 * <li>changing the global flag of search orders (TODO)</li>
 * <li>changing whether excluded papers are excluded or selected (TODO)</li>
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
    private CheckBoxX global;
    private CheckBoxX invertExclusions;
    private AjaxSubmitLink queryLink;
    private AjaxSubmitLink saveLink;

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
        makeAndQueueGlobalCheckBox("global");
        makeAndQueueQueryButton("query");
        makeAndQueueInvertExclusionsCheckBox("invertExclusions");
        makeAndQueueSaveButton("save");
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
                target.add(global);
                target.add(invertExclusions);
                target.add(queryLink);
                target.add(saveLink);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
                info("Sent SearchOrderChangeEvent");
            }
        });
        searchOrder.add(new AttributeModifier("data-width", "fit"));
        queue(searchOrder);
    }

    private void makeAndQueueGlobalCheckBox(String id) {
        global = new CheckBoxX(id, new PropertyModel<Boolean>(getModel(), SearchOrder.GLOBAL)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isUserEntitled());
            }
        };
        global.getConfig().withThreeState(false).withUseNative(true);
        queueCheckBoxAndLabel(global);
    }

    private void makeAndQueueQueryButton(String id) {
        queryLink = new AjaxSubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isModelSelected());
            }

            @Override
            protected void onAfterSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onAfterSubmit(target, form);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
            }
        };
        queryLink.add(new ButtonBehavior());
        queryLink.setBody(new StringResourceModel("button.query.label"));
        queryLink.setDefaultFormProcessing(true);
        queryLink.setOutputMarkupId(true);
        queue(queryLink);
    }

    private void makeAndQueueSaveButton(String id) {
        saveLink = new AjaxSubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isModelSelected() && SearchOrderSelectorPanel.this.isUserEntitled());
            }

            @Override
            protected void onAfterSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onAfterSubmit(target, form);
                if (getModelObject() != null) {
                    searchOrderService.saveOrUpdate(getModelObject());
                }
            }
        };
        saveLink.add(new ButtonBehavior());
        saveLink.setBody(new StringResourceModel(getSubmitLinkResourceLabel()));
        saveLink.setDefaultFormProcessing(false);
        saveLink.setEnabled(!isViewMode());
        saveLink.setOutputMarkupId(true);
        queue(saveLink);
    }

    protected boolean isModelSelected() {
        return getModelObject() != null && getModelObject().getId() != null;
    }

    protected boolean isUserEntitled() {
        return getModelObject() != null && getModelObject().getOwner() == getActiveUser().getId();
    }

    private void makeAndQueueInvertExclusionsCheckBox(String id) {
        invertExclusions = new CheckBoxX(id, new PropertyModel<Boolean>(getModel(), SearchOrder.INVERT_EXCLUSIONS));
        invertExclusions.getConfig().withThreeState(false).withUseNative(true);
        queueCheckBoxAndLabel(invertExclusions);
    }

}
