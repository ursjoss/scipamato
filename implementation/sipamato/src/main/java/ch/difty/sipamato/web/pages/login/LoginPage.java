package ch.difty.sipamato.web.pages.login;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.home.SipamatoHomePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

@MountPath("login")
@WicketSignInPage
public class LoginPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (isSignedIn()) {
            continueToOriginalDestination();
        }

        add(new LoginForm("form"));
    }

    // TODO consider using SignInPanel
    private class LoginForm extends Form<LoginForm> {
        private static final long serialVersionUID = 1L;

        private String username;
        private String password;

        public LoginForm(String id) {
            super(id);
            setDefaultModel(new CompoundPropertyModel<>(this));

            add(new Label("header", new ResourceModel("header.label", "h")));
            add(new Label("usernameLabel", new ResourceModel("username.label", "un")));
            add(new RequiredTextField<String>("username"));
            add(new Label("passwordLabel", new ResourceModel("password.label", "pw")));
            add(new PasswordTextField("password"));
            add(new BootstrapButton("signin", new ResourceModel("signin.value", "si"), Buttons.Type.Default));
        }

        @Override
        protected void onSubmit() {
            if (signIn(username, password)) {
                setResponsePage(SipamatoHomePage.class);
            } else {
                error(getString("msg.login.failure"));
            }
        }

    }
}
