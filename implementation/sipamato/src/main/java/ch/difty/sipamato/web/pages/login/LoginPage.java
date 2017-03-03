package ch.difty.sipamato.web.pages.login;

import java.util.Optional;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
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

    private String username;
    private String password;

    public LoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (isSignedIn()) {
            continueToOriginalDestination();
        }

        queue(newLoginForm("form"));
    }

    private StatelessForm<Void> newLoginForm(String id) {
        StatelessForm<Void> form = new StatelessForm<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                if (signIn(username, password)) {
                    setResponsePage(SipamatoHomePage.class);
                } else {
                    error(getString("msg.login.failure"));
                }
            }

        };
        form.setDefaultModel(new CompoundPropertyModel<>(this));

        queue(newHeader("header"));
        queueFieldAndLabel(new RequiredTextField<String>("username"), Optional.empty());
        queueFieldAndLabel(new PasswordTextField("password"), Optional.empty());
        queue(newButton("signin"));

        return form;
    }

    private Label newHeader(String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null));
    }

    private BootstrapButton newButton(String id) {
        return new BootstrapButton(id, new StringResourceModel(id + ".value", this, null), Buttons.Type.Default);
    }

}
