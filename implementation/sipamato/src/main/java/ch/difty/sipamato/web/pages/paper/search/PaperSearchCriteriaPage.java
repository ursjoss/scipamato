package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.entity.ComplexPaperFilter;
import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

/**
 * Lookalike of the PaperEditPage that works with a {@link ComplexPaperFilter} instead of a Paper entity,
 * which can be used as a kind of Query by example (QBE) functionality.
 *
 * If instantiated with a {@link CompositeComplexPaperFilter} as parameter, it will add the current query
 * specification {@link ComplexPaperFilter} to the composite.
 *
 * Submitting the page will call the {@link PaperSearchPage} handing over the updated {@link CompositeComplexPaperFilter}.
 *
 * @author u.joss
 */
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchCriteriaPage extends BasePage<ComplexPaperFilter> {

    private static final long serialVersionUID = 1L;

    private CompositeComplexPaperFilter compositeFilter = new CompositeComplexPaperFilter(null);

    /**
     * Instantiates the page using the {@link PageParameters}.
     * Initiates the default model with a new empty {@link ComplexPaperFilter}
     * and uses a freshly initiated {@link CompositeComplexPaperFilter}.
     *
     * @param parameters
     */
    public PaperSearchCriteriaPage(final PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new ComplexPaperFilter()));
    }

    /**
     * Instantiates the page with a default model (empty {@link ComplexPaperFilter}), accepting and merging in the composite
     * of all previous search filters.
     *
     * @param compositeFilter {@link CompositeComplexPaperFilter} defining the criteria of the previously defined search parts.
     */
    public PaperSearchCriteriaPage(final IModel<CompositeComplexPaperFilter> compositeFilter) {
        super(new PageParameters());
        initDefaultModel();
        this.compositeFilter.merge(compositeFilter.getObject());
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
                compositeFilter.add(getModelObject());
                setResponsePage(new PaperSearchPage(Model.of(compositeFilter)));
            }
        };
    }

}
