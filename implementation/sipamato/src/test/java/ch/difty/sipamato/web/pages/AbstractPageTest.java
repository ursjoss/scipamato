package ch.difty.sipamato.web.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.sipamato.web.SipamatoApplication;
import ch.difty.sipamato.web.pages.home.SipamatoHomePage;
import ch.difty.sipamato.web.pages.login.LoginPage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SipamatoApplication.class)
@WebAppConfiguration
public abstract class AbstractPageTest<T extends BasePage> {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private ApplicationContextMock applicationContextMock;

    @Autowired
    private SipamatoApplication application;

    private WicketTester tester;

    public ApplicationContextMock getApplicationContextMock() {
        return applicationContextMock;
    }

    public WebApplication getApplication() {
        return application;
    }

    public WicketTester getTester() {
        return tester;
    }

    @Before
    public void setUp() {
        applicationContextMock = new ApplicationContextMock();
        applicationContextMock.putBean("authenticationManager", new AuthenticationManager() {

            @Override
            public Authentication authenticate(Authentication arg0) throws AuthenticationException {
                return new TestingAuthenticationToken(USERNAME, PASSWORD, "USER", "ADMIN");
            }
        });
        application.setApplicationContext(applicationContextMock);
        tester = new WicketTester(application);
        login(USERNAME, PASSWORD);
    }

    private void login(String username, String password) {
        SecureWebSession session = (SecureWebSession) tester.getSession();
        session.signOut();
        tester.startPage(LoginPage.class);
        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("username", username);
        formTester.setValue("password", password);
        formTester.submit();
        tester.assertNoErrorMessage();
        tester.assertRenderedPage(SipamatoHomePage.class);
    }

    @Test
    public void assertPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().debugComponentTrees();
        assertSpecificComponents();
    }

    /**
     * @return  page class to be tested
     */
    protected abstract Class<T> getPageClass();

    /**
     * Override if you want to assert specific components
     */
    protected void assertSpecificComponents() {
    }

    protected void assertLabeledTextArea(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, TextArea.class);
    }

    protected void assertLabeledTextField(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, TextField.class);
    }

    protected void assertLabeledCheckBox(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, CheckBox.class);
    }

}
