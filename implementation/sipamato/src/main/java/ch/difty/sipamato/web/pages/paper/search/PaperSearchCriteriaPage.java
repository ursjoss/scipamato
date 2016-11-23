package ch.difty.sipamato.web.pages.paper.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchCriteriaPage extends BasePage<Paper> {

    private static final long serialVersionUID = 1L;

    private final Map<Long, PaperSlim> previousSearchResults = new HashMap<Long, PaperSlim>();

    @SpringBean
    private PaperSlimService service;

    /**
     * Instantiates the page directly with a handed over paper model.
     *
     * @param paperModel
     */
    public PaperSearchCriteriaPage(IModel<Paper> paperModel) {
        super(paperModel);
    }

    /**
     * Instantiates the page using the {@link PageParameters}.
     * Initiates the default model with a new empty {@link Paper} and
     * sets the previousSearchResults as an empty map.
     *
     * @param parameters
     */
    public PaperSearchCriteriaPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    /**
     * Instantiates the page with a default model (empty {@link Paper}), handing over
     * a list of previous search results.
     *
     * @param previousSearchResults map of {@link PaperSlim}s from previous searches.
     */
    public PaperSearchCriteriaPage(Map<Long, PaperSlim> previousSearchResults) {
        super(new PageParameters());
        initDefaultModel();
        addResultsOfPreviousSearchesTo(previousSearchResults);
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new Paper()));
    }

    private void addResultsOfPreviousSearchesTo(Map<Long, PaperSlim> previousSearchResults) {
        if (previousSearchResults != null) {
            this.previousSearchResults.putAll(previousSearchResults);
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
                final List<PaperSlim> currentSearchResult = service.findByExample(getModelObject());
                if (currentSearchResult != null) {
                    for (PaperSlim p : currentSearchResult) {
                        if (!previousSearchResults.containsKey(p.getId())) {
                            previousSearchResults.put(p.getId(), p);
                        }
                    }
                }
                setResponsePage(new PaperSearchPage(Model.ofMap(previousSearchResults)));
            }
        };
    }

}
