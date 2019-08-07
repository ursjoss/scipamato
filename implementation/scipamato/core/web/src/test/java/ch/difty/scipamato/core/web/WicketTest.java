package ch.difty.scipamato.core.web;

import static org.mockito.Mockito.when;

import java.util.Locale;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;
import ch.difty.scipamato.core.web.authentication.LoginPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;
import ch.difty.scipamato.core.web.security.TestUserDetailsService;

@SpringBootTest
@SuppressWarnings("SameParameterValue")
@ActiveProfiles({ "wickettest" })
public abstract class WicketTest extends AbstractWicketTest {

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "secretpw";

    private WicketTester tester;

    public WicketTester getTester() {
        return tester;
    }

    @BeforeEach
    public final void setUp() {
        application.setHeaderResponseDecorator(
            r -> new ResourceAggregator(new JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container")));

        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        when(sessionFacadeMock.getPaperIdManager()).thenReturn(itemNavigatorMock);
        Locale locale = new Locale("en_US");
        when(sessionFacadeMock.getLanguageCode()).thenReturn(locale.getLanguage());
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN)).thenReturn(currentUserIsAnyOf(Roles.ADMIN));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER)).thenReturn(currentUserIsAnyOf(Roles.USER));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.VIEWER)).thenReturn(currentUserIsAnyOf(Roles.VIEWER));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER)).thenReturn(
            currentUserIsAnyOf(Roles.ADMIN, Roles.USER));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.USER, Roles.VIEWER)).thenReturn(
            currentUserIsAnyOf(Roles.USER, Roles.VIEWER));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.VIEWER)).thenReturn(
            currentUserIsAnyOf(Roles.ADMIN, Roles.VIEWER));
        when(sessionFacadeMock.hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER, Roles.VIEWER)).thenReturn(
            currentUserIsAnyOf(Roles.ADMIN, Roles.USER, Roles.VIEWER));
        getTester()
            .getSession()
            .setLocale(locale);
        setUpHook();
        login(getUserName(), PASSWORD);
    }

    private boolean currentUserIsAnyOf(String... roles) {
        for (final String role : roles) {
            switch (getUserName()) {
            case TestUserDetailsService.USER_ADMIN:
                if (Roles.ADMIN.equals(role))
                    return true;
                break;
            case TestUserDetailsService.USER_USER:
                if (Roles.USER.equals(role))
                    return true;
                break;
            case TestUserDetailsService.USER_VIEWER:
                if (Roles.VIEWER.equals(role))
                    return true;
                break;
            }
        }
        return false;
    }

    // override if necessary
    protected String getUserName() {
        return USERNAME;
    }

    /**
     * override if needed
     */
    protected void setUpHook() {
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
        tester.assertRenderedPage(PaperListPage.class);
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

    protected void assertLabeledBootstrapSelect(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, BootstrapSelect.class);
    }

}
