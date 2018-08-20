package ch.difty.scipamato.core.web.newsletter.topic;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.persistence.OptimisticLockingException;
import ch.difty.scipamato.core.web.common.BasePageTest;

public class NewsletterTopicEditPageTest extends BasePageTest<NewsletterTopicEditPage> {

    private NewsletterTopicDefinition  ntd;
    private NewsletterTopicTranslation ntt_de, ntt_en, ntt_fr;

    @MockBean
    private NewsletterTopicService newsletterTopicServiceMock;

    @Override
    public void setUpHook() {
        ntt_de = new NewsletterTopicTranslation(1, "de", "thema1", 1);
        ntt_en = new NewsletterTopicTranslation(2, "en", "topic1", 1);
        ntt_fr = new NewsletterTopicTranslation(3, "fr", "sujet1", 1);
        ntd = new NewsletterTopicDefinition(1, "de", 1, ntt_de, ntt_en, ntt_fr);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(newsletterTopicServiceMock);
    }

    @Override
    protected NewsletterTopicEditPage makePage() {
        return new NewsletterTopicEditPage(Model.of(ntd));
    }

    @Override
    protected Class<NewsletterTopicEditPage> getPageClass() {
        return NewsletterTopicEditPage.class;
    }

    @Override
    public void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        b += ":";
        String bb = b + "translations";
        getTester().assertComponent(bb, RefreshingView.class);
        bb += ":";
        assertTranslation(bb, 1, "de", "thema1");
        assertTranslation(bb, 2, "en", "topic1");
        assertTranslation(bb, 3, "fr", "sujet1");

        getTester().assertComponent(b + "submit", BootstrapButton.class);
        getTester().assertComponent(b + "delete", BootstrapButton.class);
    }

    private void assertTranslation(final String bb, final int idx, final String langCode, final String title) {
        getTester().assertLabel(bb + idx + ":langCode", langCode);
        getTester().assertComponent(bb + idx + ":title", TextField.class);
        getTester().assertModelValue(bb + idx + ":title", title);
    }

    @Test
    public void submitting_withSuccessfulServiceCall_addsInfoMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenReturn(ntd);

        runSubmitTest();

        getTester().assertInfoMessages(
            "Successfully saved NewsletterTopic [id 1]: DE: '1806'; EN: 'topic1'; FR: 'sujet1'.");
        getTester().assertNoErrorMessage();
    }

    private void runSubmitTest() {
        getTester().startPage(new NewsletterTopicEditPage(Model.of(ntd)));

        FormTester formTester = getTester().newFormTester("form");
        formTester.setValue("translations:1:title", "1806");
        assertTranslation("form:translations:", 1, "de", "thema1");
        formTester.submit("submit");
        assertTranslation("form:translations:", 4, "de", "1806");

        verify(newsletterTopicServiceMock).saveOrUpdate(isA(NewsletterTopicDefinition.class));
    }

    @Test
    public void submitting_withUnsuccessfulServiceCall_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenReturn(null);

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Could not save NewsletterTopic [id 1].");
    }

    @Test
    public void submitting_withOptimisticLockingException_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new OptimisticLockingException("tblName", "rcd", OptimisticLockingException.Type.UPDATE));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("The tblName with id 1 has been modified concurrently "
                                        + "by another user. Please reload it and apply your changes once more.");
    }

    @Test
    public void submitting_withOtherException_addsErrorMsg() {
        when(newsletterTopicServiceMock.saveOrUpdate(isA(NewsletterTopicDefinition.class))).thenThrow(
            new RuntimeException("fooMsg"));

        runSubmitTest();

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages(
            "An unexpected error occurred when trying to save NewsletterTopic [id 1]: fooMsg");
    }

}