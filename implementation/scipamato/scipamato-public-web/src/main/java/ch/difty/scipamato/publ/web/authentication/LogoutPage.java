package ch.difty.scipamato.publ.web.authentication;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.pages.login.AbstractLogoutPage;

@SuppressWarnings("WeakerAccess")
@MountPath("logout")
public class LogoutPage extends AbstractLogoutPage<LoginPage> {

    private static final long serialVersionUID = 1L;

    public LogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected LoginPage getResponsePage() {
        return new LoginPage(new PageParameters());
    }
}
