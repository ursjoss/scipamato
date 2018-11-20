package ch.difty.scipamato.core.web.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.search.SearchCondition;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.SearchOrderService;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.paper.PageFactory;
import ch.difty.scipamato.core.web.paper.common.SearchablePaperPanel;

/**
 * Lookalike of the PaperEditPage that works with a {@link SearchCondition}
 * instead of a Paper entity, which can be used as a kind of Query by example
 * (QBE) functionality.
 * <p>
 * The page is instantiated with a model of a {@link SearchCondition} capturing
 * the specification from this form. If instantiated with a {@link SearchOrder}
 * as parameter, it will add the current query specification
 * {@link SearchCondition} to the search order.
 * <p>
 * Submitting the page will call the {@link PaperSearchPage} handing over the
 * updated {@link SearchOrder}.
 *
 * @author u.joss
 */
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN, Roles.VIEWER })
public class PaperSearchCriteriaPage extends BasePage<SearchCondition> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private SearchOrderService searchOrderService;

    @SpringBean
    private PageFactory pageFactory;

    @SuppressWarnings("WeakerAccess")
    public PaperSearchCriteriaPage(final IModel<SearchCondition> searchConditionModel, final long searchOrderId) {
        super(searchConditionModel);
        getPageParameters().add(CorePageParameters.SEARCH_ORDER_ID.getName(), searchOrderId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(makeSearchablePanel("contentPanel"));
    }

    @SuppressWarnings("SameParameterValue")
    private SearchablePaperPanel makeSearchablePanel(String id) {
        return new SearchablePaperPanel(id, getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                Long searchOrderId = getSearchOrderId();
                if (searchOrderId != null) {
                    try {
                        searchOrderService.saveOrUpdateSearchCondition(getModelObject(), searchOrderId,
                            getLanguageCode());
                        pageFactory
                            .setResponsePageToPaperSearchPageConsumer(this)
                            .accept(getPageParameters());
                    } catch (Exception ex) {
                        error(new StringResourceModel("save.error.hint", this, null)
                            .setParameters(getNullSafeId(), ex.getMessage())
                            .getString());
                    }
                }
            }
        };
    }

    private Long getSearchOrderId() {
        final StringValue sv = getPageParameters().get(CorePageParameters.SEARCH_ORDER_ID.getName());
        return sv.isNull() ? null : sv.toLong();
    }

    private String getNullSafeId() {
        return getModelObject().getId() != null ? getModelObject().getId() : "";
    }

}
