package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.service.PaperService;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.panel.paper.SearchablePaperPanel;

@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<Paper> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperSearchPage.class);

    @SpringBean
    private PaperService service;

    private SearchablePaperPanel contentPanel;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        setDefaultModel(Model.of(new Paper()));
    }

    public PaperSearchPage(IModel<Paper> paperModel) {
        super(paperModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        contentPanel = new SearchablePaperPanel("contentPanel", getModel()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onFormSubmit() {
                PaperSearchPage.this.doSearch();
            }
        };
        queue(contentPanel);
    }

    protected void doSearch() {
        LOGGER.info("TODO searching... <--------------------------------------------------");
    }

}
