package ch.difty.sipamato.web;

import org.apache.wicket.markup.html.basic.Label;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SipamatoApplication.class)
@WebAppConfiguration
public class SipamatoHomePageTest {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private WicketTester tester;

    @Autowired
    private SipamatoApplication application;

    private ApplicationContextMock applicationContextMock;

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

    public WicketTester getTester() {
        return tester;
    }

    public WebApplication getApplication() {
        return application;
    }

    public ApplicationContextMock getApplicationContextMock() {
        return applicationContextMock;
    }

    @Test
    public void testRenderMyPage() {
        tester.startPage(SipamatoHomePage.class);
        tester.assertRenderedPage(SipamatoHomePage.class);
        tester.assertComponent("message", Label.class);
    }
}
