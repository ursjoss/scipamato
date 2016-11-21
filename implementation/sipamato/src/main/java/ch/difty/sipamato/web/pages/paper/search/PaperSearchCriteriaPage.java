package ch.difty.sipamato.web.pages.paper.search;

import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchCriteriaPage extends BasePage<Paper> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperSearchCriteriaPage.class);

    @SpringBean
    private PaperSlimService service;

    private SearchablePaperPanel contentPanel;

    public PaperSearchCriteriaPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new Paper()));
    }

    public PaperSearchCriteriaPage(IModel<Paper> paperModel) {
        super(paperModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        contentPanel = new SearchablePaperPanel("contentPanel", getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                PaperSearchCriteriaPage.this.doSearch();
            }
        };
        queue(contentPanel);
    }

    protected void doSearch() {
        List<PaperSlim> candidates = service.findByExample(getModelObject());
        LOGGER.info("found {} papers matching the criteria", candidates.size());
        for (PaperSlim p : candidates) {
            LOGGER.info("- {}", p.toString());
        }
    }

}
