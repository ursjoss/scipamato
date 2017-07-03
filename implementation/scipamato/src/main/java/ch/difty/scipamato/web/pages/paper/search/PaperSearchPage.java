package ch.difty.scipamato.web.pages.paper.search;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.ScipamatoSession;
import ch.difty.scipamato.auth.Roles;
import ch.difty.scipamato.entity.SearchOrder;
import ch.difty.scipamato.lib.AssertAs;
import ch.difty.scipamato.service.SearchOrderService;
import ch.difty.scipamato.web.PageParameterNames;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.pages.paper.provider.PaperSlimBySearchOrderProvider;
import ch.difty.scipamato.web.panel.result.ResultPanel;
import ch.difty.scipamato.web.panel.search.SearchOrderChangeEvent;
import ch.difty.scipamato.web.panel.search.SearchOrderPanel;
import ch.difty.scipamato.web.panel.search.SearchOrderSelectorPanel;
import ch.difty.scipamato.web.panel.search.ToggleExclusionsEvent;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;

/**
 * The PaperSearchPage manages {@link SearchOrder}s by allowing the user to select, create or modify them
 * and see the result of the search in the result panel.
 *
 * The page forwards to the {@link PaperSearchCriteriaPage} in order to set up or amend the SearchCondition
 * contained within a {@link SearchOrder}.
 *
 * You can either pass a model of a {@link SearchOrder} to the page constructor or provide the {@code searchOrderId}
 * through the {@link PageParameters}. If it is valid, the page will load the search order from DB.
 *
 * @author u.joss
 */
@MountPath("search")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class PaperSearchPage extends BasePage<SearchOrder> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 12;

    private PaperSlimBySearchOrderProvider dataProvider;

    private SearchOrderSelectorPanel searchOrderSelectorPanel;
    private SearchOrderPanel searchOrderPanel;
    private ResultPanel resultPanel;
    private Label resultPanelLabel;

    @SpringBean
    private SearchOrderService searchOrderService;

    /**
     * Instantiates page by either:
     *
     * <ol>
     * <li>loading the {@link SearchOrder} from DB if the {@code searchOrderId} is provided in the {@link PageParameters}</li>
     * <li>applying a new empty {@link SearchOrder} to the page otherwise</li>
     * </ol>
     * Applies the showExcluded flag from the page parameters if present
     *
     * @param parameters
     */
    public PaperSearchPage(final PageParameters parameters) {
        super(parameters);
        trySettingSearchOrderModelFromDb();
    }

    /**
     * Instantiate the page with the provided model of the {@link SearchOrder}. It must have a valid id.
     * Supplements the page parameters from the model
     *
     * @param searchOrderModel
     */
    public PaperSearchPage(final IModel<SearchOrder> searchOrderModel) {
        super(searchOrderModel);
        AssertAs.notNull(searchOrderModel, "searchOrderModel");
        AssertAs.notNull(searchOrderModel.getObject(), "searchOrderModel.object");
        AssertAs.notNull(searchOrderModel.getObject().getId(), "searchOrderModel.object.id");
        getPageParameters().clearNamed();
        getPageParameters().add(PageParameterNames.SEARCH_ORDER_ID, searchOrderModel.getObject().getId());
        getPageParameters().add(PageParameterNames.SHOW_EXCLUDED, searchOrderModel.getObject().isShowExcluded());
    }

    private void trySettingSearchOrderModelFromDb() {
        final Long searchOrderId = searchOrderIdFromPageParameters();
        final Optional<SearchOrder> so = searchOrderId != null ? searchOrderService.findById(searchOrderId) : Optional.empty();
        so.ifPresent(this::setShowExcluded);
        setDefaultModel(Model.of(so.orElse(makeEmptyModelObject())));
    }

    private Long searchOrderIdFromPageParameters() {
        final StringValue sv = getPageParameters().get(PageParameterNames.SEARCH_ORDER_ID);
        return sv.isNull() ? null : sv.toLong();
    }

    private void setShowExcluded(SearchOrder so) {
        so.setShowExcluded(showExcludedFromPageParameters());
    }

    private boolean showExcludedFromPageParameters() {
        final StringValue ieString = getPageParameters().get(PageParameterNames.SHOW_EXCLUDED);
        final Boolean ie = ieString.isNull() ? null : ieString.toBoolean();
        return ie != null ? ie.booleanValue() : false;
    }

    private SearchOrder makeEmptyModelObject() {
        final SearchOrder so = new SearchOrder(null);
        so.setId(searchOrderIdFromPageParameters());
        so.setShowExcluded(showExcludedFromPageParameters());
        so.setName(null);
        so.setOwner(getActiveUser().getId());
        return so;
    }

    private SearchOrder makeNewModelObject() {
        final SearchOrder so = new SearchOrder(null);
        so.setName(null);
        so.setOwner(getActiveUser().getId());
        return so;
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new PaperSlimBySearchOrderProvider(getModelObject(), RESULT_PAGE_SIZE);

        fixShowExcluded_ifNoExclusionsPresent();

        makeSearchOrderSelectorPanel("searchOrderSelectorPanel");
        makeSearchOrderPanel("searchOrderPanel");
        makeResultPanel("resultPanel");
        updateNavigateable();
    }

    private void fixShowExcluded_ifNoExclusionsPresent() {
        if (getModelObject().isShowExcluded() && getModelObject().getExcludedPaperIds().isEmpty()) {
            getModelObject().setShowExcluded(false);
        }
    }

    private void makeSearchOrderSelectorPanel(String id) {
        queuePanelHeadingFor(id);

        searchOrderSelectorPanel = new SearchOrderSelectorPanel(id, getModel());
        searchOrderSelectorPanel.setOutputMarkupId(true);
        queue(searchOrderSelectorPanel);
    }

    private void makeSearchOrderPanel(final String id) {
        queuePanelHeadingFor(id);

        searchOrderPanel = new SearchOrderPanel(id, getModel());
        searchOrderPanel.setOutputMarkupId(true);
        queue(searchOrderPanel);
    }

    private void makeResultPanel(final String id) {
        resultPanelLabel = new Label(id + LABEL_TAG) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (PaperSearchPage.this.getModelObject() != null && PaperSearchPage.this.getModelObject().isShowExcluded()) {
                    setDefaultModel(new StringResourceModel(id + "-excluded" + PANEL_HEADER_RESOURCE_TAG, this, null));
                } else {
                    setDefaultModel(new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null));
                }
            }

        };
        resultPanelLabel.setOutputMarkupId(true);
        queue(resultPanelLabel);

        resultPanel = new ResultPanel(id, dataProvider);
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if (event.getPayload().getClass() == SearchOrderChangeEvent.class) {
            final SearchOrderChangeEvent soce = (SearchOrderChangeEvent) event.getPayload();
            handleSearchOrderEvent(soce);
            updateNavigateable();
            event.dontBroadcastDeeper();
        } else if (event.getPayload().getClass() == ToggleExclusionsEvent.class) {
            ToggleExclusionsEvent tee = (ToggleExclusionsEvent) event.getPayload();
            setShowExcludedWhereRelevant();
            updateNavigateable();
            addingToTarget(tee.getTarget());
            event.dontBroadcastDeeper();
        }
    }

    private void addingToTarget(final AjaxRequestTarget target) {
        if (target != null) {
            target.add(resultPanelLabel);
            target.add(resultPanel);
        }
    }

    private void setShowExcludedWhereRelevant() {
        final boolean oldValue = showExcludedFromPageParameters();
        getPageParameters().set(PageParameterNames.SHOW_EXCLUDED, !oldValue);
        dataProvider.setShowExcluded(!oldValue);
        getModelObject().setShowExcluded(!oldValue);
    }

    private void handleSearchOrderEvent(final SearchOrderChangeEvent soce) {
        setExclusionIntoModel(soce);
        if (soce.getDroppedConditionId() != null) {
            searchOrderService.removeSearchConditionWithId(soce.getDroppedConditionId());
        }
        resetAndSaveProviderModel(soce);
        addSubPanelsAsTarget(soce);
    }

    /* Adds or removes an excluded id - depending on whether the showEcluded flag is set in the model */
    private void setExclusionIntoModel(final SearchOrderChangeEvent soce) {
        if (soce.getExcludedId() != null) {
            if (!getModelObject().isShowExcluded()) {
                getModelObject().addExclusionOfPaperWithId(soce.getExcludedId());
            } else {
                getModelObject().removeExlusionOfPaperWithId(soce.getExcludedId());
            }
        }
    }

    private void resetAndSaveProviderModel(final SearchOrderChangeEvent soce) {
        if (getModelObject() != null && !soce.isNewSearchOrderRequested()) {
            if (soce.getExcludedId() != null) {
                SearchOrder p = searchOrderService.saveOrUpdate(getModelObject());
                setModelObject(p);
            }
            dataProvider.setFilterState(getModelObject());
            if (getModelObject().getExcludedPaperIds().isEmpty())
                updateNavigateable();

        } else {
            final SearchOrder newSearchOrder = makeNewModelObject();
            final SearchOrder persistedNewSearchOrder = searchOrderService.saveOrUpdate(newSearchOrder);
            setModelObject(persistedNewSearchOrder);
            final PageParameters pp = new PageParameters();
            pp.add(PageParameterNames.SEARCH_ORDER_ID, persistedNewSearchOrder.getId());
            pp.add(PageParameterNames.SHOW_EXCLUDED, false);
            setResponsePage(new PaperSearchPage(pp));
        }
    }

    private void addSubPanelsAsTarget(final SearchOrderChangeEvent soce) {
        final AjaxRequestTarget target = soce.getTarget();
        if (target != null) {
            target.add(searchOrderSelectorPanel);
            target.add(searchOrderPanel);
            target.add(resultPanelLabel);
            target.add(resultPanel);
        }
    }

    /**
     * Have the provider provide a list of all paper ids matching the current filter.
     * Construct a navigateable with this list and set it. set focus to null.
     */
    private void updateNavigateable() {
        ScipamatoSession.get().getPaperIdManager().setFocusToItem(null);
        ScipamatoSession.get().getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
    }

}
