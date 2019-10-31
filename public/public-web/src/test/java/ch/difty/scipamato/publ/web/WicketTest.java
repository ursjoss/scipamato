package ch.difty.scipamato.publ.web;

import static org.mockito.Mockito.when;

import java.util.Locale;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.ScipamatoPublicApplication;
import ch.difty.scipamato.publ.persistence.api.KeywordService;
import ch.difty.scipamato.publ.persistence.api.PublicPaperService;

@SpringBootTest
@SuppressWarnings("SameParameterValue")
public abstract class WicketTest {

    @Autowired
    private ScipamatoPublicApplication application;

    @Autowired
    private ApplicationContext applicationContextMock;

    @SuppressWarnings("unused")
    @Autowired
    private DateTimeService dateTimeService;

    @MockBean
    private ScipamatoWebSessionFacade sessionFacadeMock;

    @MockBean
    private ItemNavigator<Long> itemNavigatorMock;

    @MockBean
    private PublicPaperService paperServiceMock;

    @MockBean
    private KeywordService keywordServiceMock;

    private WicketTester tester;

    public WicketTester getTester() {
        return tester;
    }

    protected ItemNavigator<Long> getItemNavigator() {
        return itemNavigatorMock;
    }

    protected PublicPaperService getPaperService() {
        return paperServiceMock;
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
        getTester()
            .getSession()
            .setLocale(locale);
        setUpHook();
    }

    /**
     * override if needed
     */
    protected void setUpHook() {
    }

    protected void assertLabeledTextField(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, TextField.class);
    }

    protected void assertLabeledMultiSelect(String b, String id) {
        final String bb = b + ":" + id;
        getTester().assertComponent(bb + "Label", Label.class);
        getTester().assertComponent(bb, BootstrapMultiSelect.class);
    }
}
