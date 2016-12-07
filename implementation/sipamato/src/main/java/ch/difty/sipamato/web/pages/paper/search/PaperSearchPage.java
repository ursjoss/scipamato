package ch.difty.sipamato.web.pages.paper.search;

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
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.SearchOrderBasedSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderChangeEvent;
import ch.difty.sipamato.web.panel.search.SearchOrderPanel;
import ch.difty.sipamato.web.panel.search.SearchOrderSelectorPanel;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;

/**
 * The PaperSearchPage allows to add and combine individual search terms by instantiating the {@link PaperSearchCriteriaPage}
 * with the currently combined {@link SearchOrder}. That page will instantiate a new instance of this page
 * after adding the new search term.
 *
 * @author u.joss
 */
@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<SearchOrder> {

    private static final long serialVersionUID = 1L;

    private SearchOrderBasedSortablePaperSlimProvider dataProvider;

    private SearchOrderSelectorPanel searchOrderSelectorPanel;
    private SearchOrderPanel searchOrderPanel;
    private ResultPanel resultPanel;

    public PaperSearchPage(final PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(makeEmptyModelObject()));
    }

    private SearchOrder makeEmptyModelObject() {
        return new SearchOrder(null);
    }

    public PaperSearchPage(final IModel<SearchOrder> searchOrderModel) {
        super(searchOrderModel);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new SearchOrderBasedSortablePaperSlimProvider(getModelObject());

        makeSearchOrderSelectorPanel("searchOrderSelectorPanel");
        makeSearchOrderPanel("searchOrderPanel");
        makeResultPanel("resultPanel");
    }

    private void makeSearchOrderSelectorPanel(String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null)));

        searchOrderSelectorPanel = new SearchOrderSelectorPanel(id, getModel());
        searchOrderSelectorPanel.setOutputMarkupId(true);
        queue(searchOrderSelectorPanel);
    }

    private void makeSearchOrderPanel(final String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null)));

        searchOrderPanel = new SearchOrderPanel(id, getModel());
        searchOrderPanel.setOutputMarkupId(true);
        queue(searchOrderPanel);
    }

    private void makeResultPanel(final String id) {
        resultPanel = new ResultPanel(id, dataProvider);
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if (event.getPayload().getClass() == SearchOrderChangeEvent.class) {
            resetProviderModel();
            addSubPanelsAsTarget(event);
            event.dontBroadcastDeeper();
        }
    }

    private void resetProviderModel() {
        if (getModelObject() != null) {
            dataProvider.setFilterState(getModelObject());
        } else {
            dataProvider.setFilterState(makeEmptyModelObject());
        }
    }

    private void addSubPanelsAsTarget(final IEvent<?> event) {
        final AjaxRequestTarget target = ((SearchOrderChangeEvent) event.getPayload()).getTarget();
        if (target != null) {
            target.add(searchOrderPanel);
            target.add(resultPanel);
        }
    }

}
