package ch.difty.scipamato.web.panel.search;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.persistence.SearchOrderService;
import ch.difty.scipamato.web.model.SearchOrderModel;
import ch.difty.scipamato.web.pages.Mode;
import ch.difty.scipamato.web.panel.AbstractPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.ButtonBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

/**
 * Panel offering the user the option of:
 *
 * <ul>
 * <li>selecting from previously saved search orders via a select box</li>
 * <li>changing the name and/or global flag of search orders </li>
 * <li>changing whether excluded papers are excluded or selected</li>
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

    // HARDCODED static number of search orders to be visible in the select box. Might need to become more dynamic
    private static final int SEARCH_ORDER_MAX = 20;

    @SpringBean
    private SearchOrderService searchOrderService;

    private Form<SearchOrder> form;
    private BootstrapSelect<SearchOrder> searchOrder;
    private TextField<String> name;
    private CheckBoxX global;
    private AjaxCheckBox showExcluded;
    private Label showExcludedLabel;

    public SearchOrderSelectorPanel(String id, IModel<SearchOrder> model) {
        super(id, model, Mode.EDIT);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(String id) {
        form = new Form<>(id, new CompoundPropertyModel<>(getModel()));
        queue(form);
        makeAndQueueSearchOrderSelectBox("searchOrder");
        makeAndQueueName(SearchOrder.NAME);
        makeAndQueueGlobalCheckBox(SearchOrder.GLOBAL);
        makeAndQueueNewButton("new");
        makeAndQueueDeleteButton("delete");
        makeAndQueueShowExcludedCheckBox(SearchOrder.SHOW_EXCLUDED);
    }

    private void makeAndQueueSearchOrderSelectBox(final String id) {
        final SearchOrderModel choices = new SearchOrderModel(getActiveUser().getId(), SEARCH_ORDER_MAX);
        final IChoiceRenderer<SearchOrder> choiceRenderer = new ChoiceRenderer<>(SearchOrder.DISPLAY_VALUE, SearchOrder.ID);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withNoneSelectedText(noneSelectedModel.getObject()).withLiveSearch(true);
        searchOrder = new BootstrapSelect<SearchOrder>(id, getModel(), choices, choiceRenderer).with(config);
        searchOrder.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                modelChanged();
                target.add(global);
                target.add(name);
                target.add(showExcluded);
                target.add(showExcludedLabel);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
                info("Sent SearchOrderChangeEvent");
            }
        });
        searchOrder.add(new AttributeModifier("data-width", "fit"));
        queue(searchOrder);
    }

    private void makeAndQueueName(String id) {
        name = new TextField<>(id);
        name.setConvertEmptyInputStringToNull(true);
        name.setOutputMarkupId(true);
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        name.setLabel(labelModel);
        name.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                saveOrUpdate();
                target.add(name);
                target.add(global);
                target.add(showExcluded);
                target.add(showExcludedLabel);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
            }
        });
        queue(name);
    }

    private void makeAndQueueGlobalCheckBox(String id) {
        global = new CheckBoxX(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isUserEntitled());
            }

            @Override
            protected void onChange(Boolean value, AjaxRequestTarget target) {
                super.onChange(value, target);
                saveOrUpdate();
                target.add(searchOrder);
            }
        };
        global.setOutputMarkupId(true);
        global.getConfig().withThreeState(false).withUseNative(true);
        queueCheckBoxAndLabel(global);
    }

    private void makeAndQueueNewButton(String id) {
        AjaxSubmitLink newLink = new AjaxSubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                target.add(name);
                target.add(global);
                target.add(showExcluded);
                target.add(showExcludedLabel);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).requestingNewSearchOrder());
            }
        };
        newLink.add(new ButtonBehavior());
        newLink.setBody(new StringResourceModel("button.new.label"));
        newLink.setDefaultFormProcessing(false);
        queue(newLink);
    }

    protected void saveOrUpdate() {
        SearchOrder so = searchOrderService.saveOrUpdate(getModelObject());
        if (so != null) {
            form.setDefaultModelObject(so);
        }
        modelChanged();
    }

    protected boolean isModelSelected() {
        return getModelObject() != null && getModelObject().getId() != null;
    }

    protected boolean isUserEntitled() {
        return getModelObject() != null && getModelObject().getOwner() == getActiveUser().getId();
    }

    protected boolean hasExclusions() {
        return isModelSelected() && CollectionUtils.isNotEmpty(getModelObject().getExcludedPaperIds());
    }

    private void makeAndQueueDeleteButton(String id) {
        AjaxSubmitLink deleteLink = new AjaxSubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(SearchOrderSelectorPanel.this.isModelSelected() && SearchOrderSelectorPanel.this.isUserEntitled());
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                if (getModelObject() != null) {
                    searchOrderService.remove(getModelObject());
                    target.add(form);
                }
            }
        };
        deleteLink.add(new ButtonBehavior());
        deleteLink.setBody(new StringResourceModel("button.delete.label"));
        deleteLink.setDefaultFormProcessing(false);
        deleteLink.setEnabled(!isViewMode());
        deleteLink.setOutputMarkupId(true);
        queue(deleteLink);
    }

    private void makeAndQueueShowExcludedCheckBox(String id) {
        showExcluded = new AjaxCheckBox(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (isVisible() && !SearchOrderSelectorPanel.this.hasExclusions())
                    setModelObject(false);
                setVisible(SearchOrderSelectorPanel.this.hasExclusions());
            }

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(showExcluded);
                target.add(showExcludedLabel);
                send(getPage(), Broadcast.BREADTH, new ToggleExclusionsEvent(target));
            }
        };
        showExcluded.setOutputMarkupPlaceholderTag(true);
        queue(showExcluded);

        showExcludedLabel = new Label(id + LABEL_TAG, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(SearchOrderSelectorPanel.this.hasExclusions());
            }
        };
        showExcludedLabel.setOutputMarkupPlaceholderTag(true);
        queue(showExcludedLabel);
    }

}
