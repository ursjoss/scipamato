package ch.difty.sipamato.web.pages.paper.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchCriteriaPage extends BasePage<Paper> {

    private static final long serialVersionUID = 1L;

    private final List<Paper> accumuluatedSearchCriteria = new ArrayList<>();

    /**
     * Instantiates the page directly with a handed over paper model.
     *
     * @param paperModel
     */
    public PaperSearchCriteriaPage(final IModel<Paper> paperModel) {
        super(paperModel);
    }

    /**
     * Instantiates the page using the {@link PageParameters}.
     * Initiates the default model with a new empty {@link Paper} and
     * sets the searchCriteria as an empty list.
     *
     * @param parameters
     */
    public PaperSearchCriteriaPage(final PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    /**
     * Instantiates the page with a default model (empty {@link Paper}), handing over
     * a list of previously accumulated search criteria.
     *
     * @param accumulatedSearchCriteria list of {@link Paper}s defining other search criteria.
     */
    public PaperSearchCriteriaPage(final List<Paper> accumulatedSearchCriteria) {
        super(new PageParameters());
        initDefaultModel();
        addDefinitionsOfPreviousSearchesTo(accumulatedSearchCriteria);
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new Paper()));
    }

    private void addDefinitionsOfPreviousSearchesTo(List<Paper> accumulatedSearchCriteria) {
        if (accumulatedSearchCriteria != null) {
            this.accumuluatedSearchCriteria.addAll(accumulatedSearchCriteria);
        }
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
                accumuluatedSearchCriteria.add((Paper)getModelObject());
                setResponsePage(new PaperSearchPage(accumuluatedSearchCriteria));
            }
        };
    }

}
