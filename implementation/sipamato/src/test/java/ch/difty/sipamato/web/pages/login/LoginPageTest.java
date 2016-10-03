package ch.difty.sipamato.web.pages.login;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;

public class LoginPageTest extends BasePageTest<LoginPage> {

    @Override
    protected LoginPage makePage() {
        return new LoginPage(null);
    }

    @Override
    protected Class<LoginPage> getPageClass() {
        return LoginPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "username");
        assertLabeledTextField(b, "password");
        getTester().assertComponent(b + ":signin", BootstrapButton.class);
    }

}
