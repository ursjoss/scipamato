package ch.difty.scipamato.core.web.paper.search;

import static ch.difty.scipamato.common.web.WicketUtilsKt.LABEL_TAG;
import static ch.difty.scipamato.common.web.WicketUtilsKt.PANEL_HEADER_RESOURCE_TAG;
import static ch.difty.scipamato.core.web.CorePageParameters.*;

import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent;
import ch.difty.scipamato.core.web.paper.PageFactory;
import ch.difty.scipamato.core.web.paper.PaperSlimBySearchOrderProvider;
import ch.difty.scipamato.core.web.paper.SearchOrderChangeEvent;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

/**
 * The PaperSearchPage manages {@link SearchOrder}s by allowing the user to
 * select, create or modify them and see the result of the search in the result
 * panel.
 * <p>
 * The page forwards to the {@link PaperSearchCriteriaPage} in order to set up
 * or amend the SearchCondition contained within a {@link SearchOrder}.
 * <p>
 * You can either pass a model of a {@link SearchOrder} to the page constructor
 * or provide the {@code searchOrderId} through the {@link PageParameters}. If
 * it is valid, the page will load the search order from DB.
 *
 * @author u.joss
 */
@MountPath("search")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN, Roles.VIEWER })
@SuppressWarnings({ "SameParameterValue", "SpellCheckingInspection", "unused" })
public class PaperSearchPage extends BasePage<SearchOrder> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 10;

    private PaperSlimBySearchOrderProvider dataProvider;

    private SearchOrderSelectorPanel searchOrderSelectorPanel;
    private SearchOrderPanel         searchOrderPanel;
    private ResultPanel              resultPanel;
    private Label                    resultPanelLabel;

    private final Mode mode;

    @SpringBean
    private SearchOrderService searchOrderService;

    @SpringBean
    private PageFactory pageFactory;

    /**
     * Instantiates page by either:
     *
     * <ol>
     * <li>loading the {@link SearchOrder} from DB if the {@code searchOrderId} is
     * provided in the {@link PageParameters}</li>
     * <li>applying a new empty {@link SearchOrder} to the page otherwise</li>
     * </ol>
     * Applies the showExcluded flag from the page parameters if present
     *
     * @param parameters
     *     the page parameters
     */
    public PaperSearchPage(@Nullable final PageParameters parameters) {
        super(parameters);
        trySettingSearchOrderModelFromDb();
        this.mode = modeFromPageParametersOrUser();
    }

    /**
     * Instantiate the page with the provided model of the {@link SearchOrder}. It
     * must have a valid id. Supplements the page parameters from the model
     *
     * @param searchOrderModel
     *     the model of the searchOrder
     * @param mode
     *     the mode in which to open the page
     */
    public PaperSearchPage(@NotNull final IModel<SearchOrder> searchOrderModel, @NotNull final Mode mode) {
        super(searchOrderModel);
        final SearchOrder searchOrder = searchOrderModel.getObject();
        Objects.requireNonNull(searchOrder);
        Objects.requireNonNull(searchOrder.getId());
        getPageParameters().clearNamed();
        getPageParameters().add(SEARCH_ORDER_ID.getName(), searchOrder.getId());
        getPageParameters().add(SHOW_EXCLUDED.getName(), searchOrder.isShowExcluded());
        getPageParameters().add(MODE.getName(), mode);
        this.mode = mode;
    }

    private void trySettingSearchOrderModelFromDb() {
        final Long searchOrderId = searchOrderIdFromPageParameters();
        final Optional<SearchOrder> so = searchOrderId != null ? searchOrderService.findById(searchOrderId) : Optional.empty();
        so.ifPresent(this::setShowExcluded);
        setDefaultModel(Model.of(so.orElse(makeEmptyModelObject())));
    }

    private Long searchOrderIdFromPageParameters() {
        final StringValue sv = getPageParameters().get(SEARCH_ORDER_ID.getName());
        return sv.isNull() ? null : sv.toLong();
    }

    private Mode modeFromPageParametersOrUser() {
        final StringValue sv = getPageParameters().get(MODE.getName());
        return sv.isNull() ? modeFromUserRole() : Mode.valueOf(sv.toString());
    }

    private Mode modeFromUserRole() {
        return hasOneOfRoles(Roles.USER, Roles.ADMIN) ? Mode.EDIT : Mode.VIEW;
    }

    private void setShowExcluded(SearchOrder so) {
        so.setShowExcluded(showExcludedFromPageParameters());
    }

    private boolean showExcludedFromPageParameters() {
        final StringValue ieString = getPageParameters().get(SHOW_EXCLUDED.getName());
        final Boolean ie = ieString.isNull() ? null : ieString.toBoolean();
        return ie != null && ie;
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
    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new PaperSlimBySearchOrderProvider(getModelObject(), RESULT_PAGE_SIZE);

        fixShowExcludedInCaseOfNoExclusionsPresent();

        makeSearchOrderSelectorPanel("searchOrderSelectorPanel");
        makeSearchOrderPanel("searchOrderPanel");
        makeResultPanel("resultPanel");
        updateNavigateable();
    }

    private void fixShowExcludedInCaseOfNoExclusionsPresent() {
        if (getModelObject().isShowExcluded() && getModelObject()
            .getExcludedPaperIds()
            .isEmpty()) {
            getModelObject().setShowExcluded(false);
        }
    }

    private void makeSearchOrderSelectorPanel(String id) {
        queuePanelHeadingFor(id);

        searchOrderSelectorPanel = new SearchOrderSelectorPanel(id, getModel(), mode);
        searchOrderSelectorPanel.setOutputMarkupId(true);
        queue(searchOrderSelectorPanel);
    }

    private void makeSearchOrderPanel(final String id) {
        queuePanelHeadingFor(id);

        searchOrderPanel = new SearchOrderPanel(id, getModel(), mode);
        searchOrderPanel.setOutputMarkupId(true);
        queue(searchOrderPanel);
    }

    private void makeResultPanel(final String id) {
        resultPanelLabel = new Label(id + LABEL_TAG) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                if (PaperSearchPage.this.getModelObject() != null && PaperSearchPage.this
                    .getModelObject()
                    .isShowExcluded()) {
                    setDefaultModel(new StringResourceModel(id + "-excluded" + PANEL_HEADER_RESOURCE_TAG, this, null));
                } else {
                    setDefaultModel(new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null));
                }
            }

        };
        resultPanelLabel.setOutputMarkupId(true);
        queue(resultPanelLabel);

        resultPanel = new ResultPanel(id, dataProvider, mode) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean isOfferingSearchComposition() {
                return true;
            }
        };
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

    @Override
    public void onEvent(@NotNull final IEvent<?> event) {
        if (event
                .getPayload()
                .getClass() == SearchOrderChangeEvent.class)
            manageSearchOrderChange(event);
        else if (event
                     .getPayload()
                     .getClass() == ToggleExclusionsEvent.class)
            manageToggleExclusion(event);
        else if (event
                     .getPayload()
                     .getClass() == NewsletterChangeEvent.class)
            manageNewsletterChangeEvent(event);
    }

    private void manageSearchOrderChange(final IEvent<?> event) {
        final SearchOrderChangeEvent soce = (SearchOrderChangeEvent) event.getPayload();
        handleSearchOrderChangeEvent(soce);
        updateNavigateable();
        event.dontBroadcastDeeper();
    }

    private void handleSearchOrderChangeEvent(final SearchOrderChangeEvent soce) {
        setExclusionIntoModel(soce);
        if (soce.getDroppedConditionId() != null)
            searchOrderService.removeSearchConditionWithId(soce.getDroppedConditionId());
        resetAndSaveProviderModel(soce);
        addSubPanelsAsTarget(soce);
    }

    private void manageNewsletterChangeEvent(final IEvent<?> event) {
        final AjaxRequestTarget target = ((NewsletterChangeEvent) event.getPayload()).getTarget();
        target.add(getFeedbackPanel());
        event.dontBroadcastDeeper();
    }

    /*
     * Adds or removes an excluded id - depending on whether the showExcluded flag is
     * set in the model
     */
    private void setExclusionIntoModel(final SearchOrderChangeEvent soce) {
        if (soce.getExcludedId() != null) {
            if (!getModelObject().isShowExcluded()) {
                getModelObject().addExclusionOfPaperWithId(soce.getExcludedId());
            } else {
                getModelObject().removeExclusionOfPaperWithId(soce.getExcludedId());
            }
        }
    }

    private void resetAndSaveProviderModel(final SearchOrderChangeEvent soce) {
        if (getModelObject() != null && !soce.isNewSearchOrderRequested()) {
            updateExistingSearchOrder(soce);
        } else {
            newSearchOrder();
        }
    }

    private void updateExistingSearchOrder(final SearchOrderChangeEvent soce) {
        if (soce.getExcludedId() != null) {
            try {
                SearchOrder p = searchOrderService.saveOrUpdate(getModelObject());
                setModelObject(p);
            } catch (OptimisticLockingException ole) {
                handleOptimisticLockingException(ole);
            }
        }
        dataProvider.setFilterState(getModelObject());
        if (getModelObject() != null && getModelObject()
            .getExcludedPaperIds()
            .isEmpty())
            updateNavigateable();
    }

    private void handleOptimisticLockingException(final OptimisticLockingException ole) {
        final String msg = new StringResourceModel("save.optimisticlockexception.hint", this, null)
            .setParameters(ole.getTableName(), getModelObject().getId())
            .getString();
        log.error(msg);
        error(msg);
    }

    private void newSearchOrder() {
        final SearchOrder newSearchOrder = makeNewModelObject();
        try {
            final SearchOrder persistedNewSearchOrder = searchOrderService.saveOrUpdate(newSearchOrder);
            setModelObject(persistedNewSearchOrder);
            final PageParameters pp = new PageParameters();
            pp.add(SEARCH_ORDER_ID.getName(), persistedNewSearchOrder.getId());
            pp.add(SHOW_EXCLUDED.getName(), false);
            pp.add(MODE.getName(), mode);
            pageFactory.setResponsePageToPaperSearchPageConsumer(this);
        } catch (OptimisticLockingException ole) {
            handleOptimisticLockingException(ole);
        }
    }

    private void manageToggleExclusion(final IEvent<?> event) {
        ToggleExclusionsEvent tee = (ToggleExclusionsEvent) event.getPayload();
        setShowExcludedWhereRelevant();
        updateNavigateable();
        addingToTarget(tee.getTarget());
        event.dontBroadcastDeeper();
    }

    private void setShowExcludedWhereRelevant() {
        final boolean oldValue = showExcludedFromPageParameters();
        getPageParameters().set(SHOW_EXCLUDED.getName(), !oldValue);
        dataProvider.setShowExcluded(!oldValue);
        getModelObject().setShowExcluded(!oldValue);
    }

    private void addingToTarget(final AjaxRequestTarget target) {
        if (target != null) {
            target.add(resultPanelLabel);
            target.add(resultPanel);
        }
    }

    private void addSubPanelsAsTarget(final SearchOrderChangeEvent soce) {
        final AjaxRequestTarget target = soce.getTarget();
        target.add(searchOrderSelectorPanel);
        target.add(searchOrderPanel);
        target.add(resultPanelLabel);
        target.add(resultPanel);
    }

    /**
     * Have the provider provide a list of all paper ids matching the current
     * filter. Construct a navigateable with this list and set it. set focus to
     * null.
     */
    private void updateNavigateable() {
        getPaperIdManager().setFocusToItem(null);
        getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
    }
}
