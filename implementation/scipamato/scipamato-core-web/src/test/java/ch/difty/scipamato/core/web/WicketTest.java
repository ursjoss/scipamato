package ch.difty.scipamato.core.web;

import static org.mockito.Mockito.when;

import java.util.Locale;

import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.ScipamatoCoreApplication;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.PaperSlimService;
import ch.difty.scipamato.core.web.authentication.LoginPage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;

@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class WicketTest {

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "secretpw";

    @Autowired
    private ScipamatoCoreApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    @Autowired
    private DateTimeService dateTimeService;

    @MockBean
    private ScipamatoWebSessionFacade sessionFacadeMock;

    @MockBean
    private ItemNavigator<Long> itemNavigatorMock;

    // The paper slim service, paper service and newsletter service are used in the home page
    // PaperListPage
    @MockBean
    protected PaperSlimService  paperSlimServiceMock;
    @MockBean
    protected PaperService      paperServiceMock;
    @MockBean
    protected NewsletterService newsletterServiceMock;

    private WicketTester tester;

    public WebApplication getApplication() {
        return application;
    }

    public WicketTester getTester() {
        return tester;
    }

    public DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    @Before
    public final void setUp() {
        application.setHeaderResponseDecorator(
            r -> new ResourceAggregator(new JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container")));

        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock);
        tester = new WicketTester(application);
        when(sessionFacadeMock.getPaperIdManager()).thenReturn(itemNavigatorMock);
        Locale locale = new Locale("en_US");
        when(sessionFacadeMock.getLanguageCode()).thenReturn(locale.getLanguage());
        getTester()
            .getSession()
            .setLocale(locale);
        setUpHook();
        login(USERNAME, PASSWORD);
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

    protected void assertLabeledMultiSelect(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, BootstrapMultiSelect.class);
    }

}
