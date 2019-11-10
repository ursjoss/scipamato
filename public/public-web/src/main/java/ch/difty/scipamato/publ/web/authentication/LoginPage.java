package ch.difty.scipamato.publ.web.authentication;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;
import ch.difty.scipamato.publ.web.paper.browse.PublicPage;

@MountPath("login")
@WicketSignInPage
public class LoginPage extends AbstractLoginPage<PublicPage> {

    private static final long serialVersionUID = 1L;

    public LoginPage(@Nullable PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected PublicPage getResponsePage() {
        return new PublicPage(getPageParameters());
    }
}
