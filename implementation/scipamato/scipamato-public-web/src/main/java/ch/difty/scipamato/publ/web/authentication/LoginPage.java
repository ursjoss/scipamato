package ch.difty.scipamato.publ.web.authentication;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPage;

@SuppressWarnings("WeakerAccess")
@MountPath("login")
@WicketSignInPage
public class LoginPage extends AbstractLoginPage {

    private static final long serialVersionUID = 1L;

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected BasePage<Void> getResponsePage() {
        return new PublicPage(getPageParameters());
    }

}
