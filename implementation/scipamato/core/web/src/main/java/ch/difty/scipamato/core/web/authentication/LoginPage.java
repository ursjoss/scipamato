package ch.difty.scipamato.core.web.authentication;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;

@MountPath("login")
@WicketSignInPage
@SuppressWarnings("WeakerAccess")
public class LoginPage extends AbstractLoginPage<PaperListPage> {

    private static final long serialVersionUID = 1L;

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected PaperListPage getResponsePage() {
        return new PaperListPage(getPageParameters());
    }

}
