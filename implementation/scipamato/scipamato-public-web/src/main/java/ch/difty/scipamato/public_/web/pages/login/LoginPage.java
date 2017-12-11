package ch.difty.scipamato.public_.web.pages.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;
import ch.difty.scipamato.public_.web.pages.BasePage;
import ch.difty.scipamato.public_.web.pages.portal.PublicPage;

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
