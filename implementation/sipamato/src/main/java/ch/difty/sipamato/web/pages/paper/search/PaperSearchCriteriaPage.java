package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.service.SearchOrderService;
import ch.difty.sipamato.web.PageParameterNames;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

/**
 * Lookalike of the PaperEditPage that works with a {@link SearchCondition} instead of a Paper entity,
 * which can be used as a kind of Query by example (QBE) functionality.
 *
 * The page is instantiated with a model of a {@link SearchCondition} capturing the specification from this form.
 * If instantiated with a {@link SearchOrder} as parameter, it will add the current query
 * specification {@link SearchCondition} to the search order.
 *
 * Submitting the page will call the {@link PaperSearchPage} handing over the updated {@link SearchOrder}.
 *
 * @author u.joss
 */
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchCriteriaPage extends BasePage<SearchCondition> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private SearchOrderService searchOrderService;

    public PaperSearchCriteriaPage(final PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(new SearchCondition()));
    }

    /**
     * Instantiates the page with a default model (empty {@link SearchCondition}), accepting and merging in
     * the {@link SearchOrder} of all previous specification steps.
     *
     * @param searchOrderModel the model of the {@link SearchOrder} defining the criteria of the previously defined search steps.
     * @param searchConditionModel the model of the {@link SearchCondition capturing the search terms from this form
     */
    public PaperSearchCriteriaPage(final IModel<SearchCondition> searchConditionModel, final long searchOrderId) {
        super(searchConditionModel);
        getPageParameters().add(PageParameterNames.SEARCH_ORDER_ID, searchOrderId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(makeSearchablePanel("contentPanel"));
    }

    private SearchablePaperPanel makeSearchablePanel(String id) {
        return new SearchablePaperPanel(id, getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                searchOrderService.saveOrUpdateSearchCondition(getModelObject(), getSearchOrderId());
                setResponsePage(new PaperSearchPage(getPageParameters()));
            }
        };
    }

    private Long getSearchOrderId() {
        final StringValue sv = getPageParameters().get(PageParameterNames.SEARCH_ORDER_ID);
        return sv.isNull() ? null : sv.toLong();
    }

}
