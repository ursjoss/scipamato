package ch.difty.sipamato.web.panel.search;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
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
 * <li>changing the global flag of search orders </li>
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

    @SpringBean
    private SearchOrderService searchOrderService;

    private Form<SearchOrder> form;
    private BootstrapSelect<SearchOrder> searchOrder;
    private TextField<String> name;
    private CheckBoxX global;
    private CheckBoxX invertExclusions;
    private AjaxSubmitLink queryLink;
    private AjaxSubmitLink newLink;
    private AjaxSubmitLink saveLink;
    private AjaxSubmitLink deleteLink;

    public SearchOrderSelectorPanel(String id, IModel<SearchOrder> model) {
        super(id, model, Mode.EDIT);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        queueForm("form");
    }

    private void queueForm(String id) {
        form = new Form<SearchOrder>(id, new CompoundPropertyModel<SearchOrder>(getModel()));
        queue(form);
        makeAndQueueSearchOrderSelectBox("searchOrder");
        makeAndQueueName(SearchOrder.NAME);
        makeAndQueueGlobalCheckBox(SearchOrder.GLOBAL);
        makeAndQueueInvertExclusionsCheckBox(SearchOrder.INVERT_EXCLUSIONS);
        makeAndQueueQueryButton("query");
        makeAndQueueNewButton("new");
        makeAndQueueSaveButton("save");
        makeAndQueueDeleteButton("delete");
    }

    private void makeAndQueueSearchOrderSelectBox(final String id) {
        final SearchOrderModel choices = new SearchOrderModel(getActiveUser().getId());
        final IChoiceRenderer<SearchOrder> choiceRenderer = new ChoiceRenderer<SearchOrder>(SearchOrder.DISPLAY_VALUE, SearchOrder.ID);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withNoneSelectedText(noneSelectedModel.getObject()).withLiveSearch(true);
        searchOrder = new BootstrapSelect<SearchOrder>(id, getModel(), choices, choiceRenderer).with(config);
        searchOrder.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                modelChanged();
                target.add(global);
                target.add(invertExclusions);
                target.add(queryLink);
                target.add(saveLink);
                target.add(name);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
                info("Sent SearchOrderChangeEvent");
            }
        });
        searchOrder.add(new AttributeModifier("data-width", "fit"));
        queue(searchOrder);
    }

    private void makeAndQueueName(String id) {
        name = new TextField<String>(id);
        name.setConvertEmptyInputStringToNull(true);
        name.setOutputMarkupId(true);
        name.setLabel(new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null));
        name.add(new AjaxFormComponentUpdatingBehavior(CHANGE) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                modelChanged();
                target.add(name);
                target.add(global);
                target.add(invertExclusions);
                target.add(queryLink);
                target.add(saveLink);
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
        };
        global.setOutputMarkupId(true);
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
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
            }
        };
        queryLink.add(new ButtonBehavior());
        queryLink.setBody(new StringResourceModel("button.query.label"));
        queryLink.setDefaultFormProcessing(true);
        queryLink.setOutputMarkupId(true);
        queue(queryLink);
    }

    private void makeAndQueueNewButton(String id) {
        newLink = new AjaxSubmitLink(id, form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target).requestingNewSearchOrder());
            }
        };
        newLink.add(new ButtonBehavior());
        newLink.setBody(new StringResourceModel("button.new.label"));
        newLink.setDefaultFormProcessing(false);
        queue(newLink);
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
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                if (getModelObject() != null) {
                    modelChanged();
                    SearchOrder so = searchOrderService.saveOrUpdate(getModelObject());
                    if (so != null) {
                        getForm().setDefaultModelObject(so);
                    }
                    target.add(searchOrder);
                    target.add(name);
                    target.add(global);
                    send(getPage(), Broadcast.BREADTH, new SearchOrderChangeEvent(target));
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

    protected boolean hasExclusions() {
        return getModelObject() != null && CollectionUtils.isNotEmpty(getModelObject().getExcludedPaperIds());
    }

    private void makeAndQueueInvertExclusionsCheckBox(String id) {
        invertExclusions = new CheckBoxX(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                final boolean hasExclusions = SearchOrderSelectorPanel.this.hasExclusions();
                setEnabled(hasExclusions);
                if (!hasExclusions) {
                    final SearchOrder so = SearchOrderSelectorPanel.this.getModelObject();
                    if (so != null)
                        so.setInvertExclusions(false);
                }
            }
        };
        invertExclusions.getConfig().withThreeState(false).withUseNative(true);
        queueCheckBoxAndLabel(invertExclusions);
    }

    private void makeAndQueueDeleteButton(String id) {
        deleteLink = new AjaxSubmitLink(id, form) {
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
}
