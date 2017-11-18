package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.web.AbstractPage;

@MountPath("logout")
public class LogoutPage extends AbstractLogoutPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties scipapamtoProperties;

    public LogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected ApplicationProperties getProperties() {
        return scipapamtoProperties;
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return new LoginPage(new PageParameters());
    }
}
