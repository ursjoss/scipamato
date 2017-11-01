package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.scipamato.web.pages.BasePageTest;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;

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

    @Test
    public void whenSigningIn_withWrongCredentials_providesErrorMessage() {
        SecureWebSession session = (SecureWebSession) getTester().getSession();
        session.signOut();
        getTester().startPage(LoginPage.class);
        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("username", "testuser");
        formTester.setValue("password", "wrongpw");
        formTester.submit();
        getTester().assertErrorMessages("The username and/or password were invalid.");
        getTester().assertRenderedPage(getPageClass());

        assertMenuEntries();
    }

    private void assertMenuEntries() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertComponent("navbar", Navbar.class);
        assertPageLinkButton(0, NavbarButton.class, "Papers");
        assertPageLinkButton(1, NavbarButton.class, "Search");
        assertPageLinkButton(2, NavbarButton.class, "Logout");

        getTester().assertComponent("navbar:container:collapse:navRightListEnclosure:navRightList:0:component", NavbarExternalLink.class);
        getTester().assertModelValue("navbar:container:collapse:navRightListEnclosure:navRightList:0:component", "https://github.com/ursjoss/scipamato/wiki/");
    }

    private void assertPageLinkButton(int index, Class<? extends Component> expectedComponentClass, String expectedLabelText) {
        String path = "navbar:container:collapse:navLeftListEnclosure:navLeftList:" + index + ":component";
        getTester().assertComponent(path, NavbarButton.class);
        getTester().assertLabel(path + ":label", expectedLabelText);
    }
}
