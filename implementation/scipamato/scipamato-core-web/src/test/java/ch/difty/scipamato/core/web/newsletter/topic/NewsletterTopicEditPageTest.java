package ch.difty.scipamato.core.web.newsletter.topic;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicTranslation;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.result.ResultPanel;

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

//        b += ":";
//        getTester().assertLabel(b + "issueLabel", "Issue");
//        getTester().assertComponent(b + "issue", TextField.class);
//
//        getTester().assertLabel(b + "issueDateLegacyLabel", "Issue Date");
//        getTester().assertComponent(b + "issueDateLegacy", DateTextField.class);
//
//        getTester().assertLabel(b + "publicationStatusLabel", "Publication Status");
//        getTester().assertComponent(b + "publicationStatus", BootstrapSelect.class);
//        getTester().assertEnabled(b + "publicationStatus");
//
//        getTester().assertComponent(b + "submit", BootstrapButton.class);
//
//        getTester().assertComponent("resultPanel", ResultPanel.class);
    }

//    @Test
//    public void submitting_callsService() {
//        getTester().startPage(NewsletterTopicEditPage.class);
//
//        FormTester formTester = getTester().newFormTester("form");
//        formTester.setValue("issue", "1806");
//        formTester.submit("submit");
//
//        verify(newsletterTopicServiceMock).saveOrUpdate(isA(Newsletter.class));
//    }

}