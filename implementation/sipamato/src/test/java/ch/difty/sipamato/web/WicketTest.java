package ch.difty.sipamato.web;

import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import ch.difty.sipamato.SipamatoApplication;
import ch.difty.sipamato.web.pages.home.SipamatoHomePage;
import ch.difty.sipamato.web.pages.login.LoginPage;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;

@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class WicketTest {

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "secretpw";

    @Autowired
    private SipamatoApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    @SpyBean
    private CustomAuthenticationManager customAuthenticationManager;

    private WicketTester tester;

    public WebApplication getApplication() {
        return application;
    }

    public WicketTester getTester() {
        return tester;
    }

    @Before
    public final void setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        Locale locale = new Locale("en_US");
        tester.getSession().setLocale(locale);
        setUpHook();
        login(USERNAME, PASSWORD);
    }

    /**
     * override if needed
     */
    protected void setUpHook() {
    }

    public static class CustomAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication arg0) throws AuthenticationException {
            return new TestingAuthenticationToken(USERNAME, PASSWORD, "USER", "ADMIN");
        }
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

    protected void assertLabeledCheckBoxX(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, CheckBoxX.class);
    }

    protected void assertLabeledMultiSelect(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, BootstrapMultiSelect.class);
    }

}
