package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.service.PaperSlimService;
import ch.difty.sipamato.web.pages.BasePage;

@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<PaperSlim> {

    private static final long serialVersionUID = 1L;

    private PaperComplexFilter filter;

    @SpringBean
    private PaperSlimService paperSlimService;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        filter = new PaperComplexFilter();
    }

    protected void onInitialize() {
        super.onInitialize();

        queue(new Form<>("form"));
        queueResponsePageButton("addSearch", new PaperSearchCriteriaPage(getPageParameters()));
    }

}
