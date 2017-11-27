package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.web.AbstractPage;

@MountPath("logout")
@AuthorizeInstantiation({ "ROLE_USER", "ROLE_ADMIN" })
public class LogoutPage extends AbstractLogoutPage {

    private static final long serialVersionUID = 1L;

    public LogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return new LoginPage(new PageParameters());
    }
}
