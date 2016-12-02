package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.SearchOrderBasedSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;
import ch.difty.sipamato.web.panel.search.SearchTermModifiedEvent;
import ch.difty.sipamato.web.panel.search.SearchTermPanel;
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
    private SearchTermPanel searchTermPanel;
    private ResultPanel resultPanel;

    public PaperSearchPage(final PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(new SearchOrder(null)));
    }

    public PaperSearchPage(final IModel<SearchOrder> filterModel) {
        super(filterModel);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    protected void onInitialize() {
        super.onInitialize();

        dataProvider = new SearchOrderBasedSortablePaperSlimProvider(getModelObject());

        makeSearchTermPanel("searchTermPanel");
        makeResultPanel("resultPanel");
    }

    private void makeSearchTermPanel(final String id) {
        searchTermPanel = new SearchTermPanel(id, getModel(), dataProvider);
        searchTermPanel.setOutputMarkupId(true);
        queue(searchTermPanel);
    }

    private void makeResultPanel(final String id) {
        resultPanel = new ResultPanel(id, dataProvider);
        resultPanel.setOutputMarkupId(true);
        queue(resultPanel);
    }

    @Override
    public void onEvent(final IEvent<?> event) {
        if (event.getPayload().getClass() == SearchTermModifiedEvent.class) {
            // Refresh the result panel upon changes in the searchTermPanel
            final AjaxRequestTarget target = ((SearchTermModifiedEvent) event.getPayload()).getTarget();
            if (target != null) {
                target.add(resultPanel);
            }
            event.dontBroadcastDeeper();
        }
    }

}
