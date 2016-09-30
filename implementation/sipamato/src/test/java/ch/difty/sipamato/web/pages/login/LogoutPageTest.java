package ch.difty.sipamato.web.pages.login;

import org.apache.wicket.markup.html.form.Form;

import ch.difty.sipamato.web.pages.AbstractPageTest;

public class LogoutPageTest extends AbstractPageTest<LogoutPage> {

    @Override
    protected LogoutPage makePage() {
        return new LogoutPage(null);
    }

    @Override
    protected Class<LogoutPage> getPageClass() {
        return LogoutPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);
    }

}
