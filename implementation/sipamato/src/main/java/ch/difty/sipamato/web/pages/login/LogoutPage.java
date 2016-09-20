package ch.difty.sipamato.web.pages.login;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.web.pages.BasePage;

@MountPath("logout")
@AuthorizeInstantiation("USER")
public class LogoutPage extends BasePage {

    public LogoutPage(PageParameters parameters) {
        super(parameters);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new LogoutForm("form"));
    }

    private static class LogoutForm extends Form<LogoutForm> {
        private static final long serialVersionUID = 1L;

        public LogoutForm(String id) {
            super(id);
        }

        @Override
        protected void onSubmit() {
            AuthenticatedWebSession.get().invalidate();
            setResponsePage(getApplication().getHomePage());
        }
    }
}
