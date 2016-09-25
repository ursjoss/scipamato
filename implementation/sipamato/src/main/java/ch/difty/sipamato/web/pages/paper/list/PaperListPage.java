package ch.difty.sipamato.web.pages.paper.list;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.web.pages.BasePage;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
    }

    protected void onInitialize() {
        super.onInitialize();
    }

}
