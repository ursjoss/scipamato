package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.web.AbstractPage;
import ch.difty.scipamato.web.pages.paper.list.PaperListPage;

@MountPath("login")
@WicketSignInPage
public class LoginPage extends AbstractLoginPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties scipapamtoProperties;

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected ApplicationProperties getProperties() {
        return scipapamtoProperties;
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return new PaperListPage(getPageParameters());
    }

}
