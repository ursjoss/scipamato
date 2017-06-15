package ch.difty.sipamato.web.pages.paper.search;

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

import ch.difty.sipamato.SipamatoSession;
import ch.difty.sipamato.auth.Roles;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.lib.AssertAs;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.PageParameterNames;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.PaperSlimBySearchOrderProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderChangeEvent;
import ch.difty.sipamato.web.panel.search.SearchOrderPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderSelectorPanel;
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
     *
     * @param parameters
     */
    public PaperSearchPage(final PageParameters parameters) {
        super(parameters);
        trySettingSearchOrderModelFromDb();
    }

    /**
     * Instantiate the page with the provided model of the {@link SearchOrder}. It must have a valid id.
     *
     * @param searchOrderModel
     */
    public PaperSearchPage(final IModel<SearchOrder> searchOrderModel) {
        super(searchOrderModel);
        AssertAs.notNull(searchOrderModel, "searchOrderModel");
        AssertAs.notNull(searchOrderModel.getObject(), "searchOrderModel.object");
        AssertAs.notNull(searchOrderModel.getObject().getId(), "searchOrderModel.object.id");
        getPageParameters().add(PageParameterNames.SEARCH_ORDER_ID, searchOrderModel.getObject().getId());
    }

    private void trySettingSearchOrderModelFromDb() {
        final Long searchOrderId = getSearchOrderId();
        final Optional<SearchOrder> so = searchOrderId != null ? searchOrderService.findById(searchOrderId) : Optional.empty();
        setDefaultModel(Model.of(so.orElse(makeEmptyModelObject())));
    }

    private Long getSearchOrderId() {
        final StringValue sv = getPageParameters().get(PageParameterNames.SEARCH_ORDER_ID);
        return sv.isNull() ? null : sv.toLong();
    }

    private SearchOrder makeEmptyModelObject() {
        final SearchOrder so = new SearchOrder(null);
        so.setId(getSearchOrderId());
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

        makeSearchOrderSelectorPanel("searchOrderSelectorPanel");
        makeSearchOrderPanel("searchOrderPanel");
        makeResultPanel("resultPanel");
        updateNavigateable();
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
                if (PaperSearchPage.this.getModelObject() != null && PaperSearchPage.this.getModelObject().isInvertExclusions()) {
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
            handleSearchOrderEvent((SearchOrderChangeEvent) event.getPayload());
            updateNavigateable();
            event.dontBroadcastDeeper();
        }
    }

    private void handleSearchOrderEvent(final SearchOrderChangeEvent soce) {
        setExclusionIntoModel(soce);
        if (soce.getDroppedConditionId() != null) {
            searchOrderService.removeSearchConditionWithId(soce.getDroppedConditionId());
        }
        resetProviderModel(soce);
        addSubPanelsAsTarget(soce);
    }

    /* Adds or removes an excluded id - depending on whether the invertExclusion flag is set in the model */
    private void setExclusionIntoModel(final SearchOrderChangeEvent soce) {
        if (soce.getExcludedId() != null) {
            if (!getModelObject().isInvertExclusions()) {
                getModelObject().addExclusionOfPaperWithId(soce.getExcludedId());
            } else {
                getModelObject().removeExlusionOfPaperWithId(soce.getExcludedId());
            }
        }
    }

    private void resetProviderModel(final SearchOrderChangeEvent soce) {
        if (getModelObject() != null && !soce.isNewSearchOrderRequested()) {
            dataProvider.setFilterState(getModelObject());
        } else {
            final SearchOrder newSearchOrder = makeNewModelObject();
            final SearchOrder persistedNewSearchOrder = searchOrderService.saveOrUpdate(newSearchOrder);
            final PageParameters pp = new PageParameters();
            pp.add(PageParameterNames.SEARCH_ORDER_ID, persistedNewSearchOrder.getId());
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
     * Construct a navigateable with this list and set it into the 
     */
    private void updateNavigateable() {
        SipamatoSession.get().getPaperIdManager().initialize(dataProvider.findAllPaperIdsByFilter());
    }

}
