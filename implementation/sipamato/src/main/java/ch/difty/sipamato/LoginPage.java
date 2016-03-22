package ch.difty.sipamato;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class LoginPage extends BasePage {

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

            add(new Label("usernameLabel", new ResourceModel("username.label", "un")));
            add(new RequiredTextField<String>("username"));
            add(new Label("passwordLabel", new ResourceModel("password.label", "pw")));
            add(new PasswordTextField("password"));
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
