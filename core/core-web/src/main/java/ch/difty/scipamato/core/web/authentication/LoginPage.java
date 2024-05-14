package ch.difty.scipamato.core.web.authentication;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;

@MountPath("login")
@WicketSignInPage
public class LoginPage extends AbstractLoginPage<PaperListPage> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    public LoginPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    @Override
    protected PaperListPage getResponsePage() {
        return new PaperListPage(getPageParameters());
    }
}
