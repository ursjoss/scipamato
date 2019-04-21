package ch.difty.scipamato.core.web.newsletter.list;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.authentication.LoginPage;
import ch.difty.scipamato.core.web.common.BasePageTest;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;

public class NewsletterTopicSortPageTest extends BasePageTest<NewsletterTopicSortPage> {

    private Newsletter newsletter;

    @MockBean
    private NewsletterTopicService service;

    private final List<NewsletterNewsletterTopic> topics = new ArrayList<>();

    @Override
    protected void setUpHook() {
        super.setUpHook();
        newsletter = new Newsletter("18/06", LocalDate.now(), PublicationStatus.WIP);
        newsletter.setId(1);

        topics.add(new NewsletterNewsletterTopic(newsletter.getId(), 1, 0, "topic1"));
        topics.add(new NewsletterNewsletterTopic(newsletter.getId(), 2, 1, "topic2"));

        when(service.getSortedNewsletterTopicsForNewsletter(newsletter.getId())).thenReturn(topics);
    }

    @Override
    protected NewsletterTopicSortPage makePage() {
        return new NewsletterTopicSortPage(Model.of(newsletter), null);
    }

    @Override
    protected Class<NewsletterTopicSortPage> getPageClass() {
        return NewsletterTopicSortPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        String b = "form";
        getTester().assertComponent(b, Form.class);

        String bb = b + ":sortable";
        getTester().assertComponent(bb, Sortable.class);

        bb += ":items:";
        for (int i = 0; i < topics.size(); i++)
            getTester().assertLabel(bb + i + ":item", "topic" + (i + 1));

        bb = b + ":submit";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertLabel(bb + ":label", "Save");

        bb = b + ":cancel";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertLabel(bb + ":label", "Cancel");
    }

    @Test
    public void startingPageWithNonNullModel__loadsSortedNewsletterTopics() {
        getTester().startPage(new NewsletterTopicSortPage(Model.of(newsletter), null));
        verify(service).getSortedNewsletterTopicsForNewsletter(newsletter.getId());
    }

    @Test
    public void startingPageWithNonModel__loadsSortedNewsletterTopics() {
        getTester().startPage(new NewsletterTopicSortPage(null, null));
        verify(service, never()).getSortedNewsletterTopicsForNewsletter(anyInt());
    }

    @Test
    public void clickSubmit_withNoPreviousPage_callsSave_andNavigatesToPaperListPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:submit", "click");

        getTester().assertRenderedPage(PaperListPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service).saveSortedNewsletterTopics(newsletter.getId(), topics);
    }

    @Test
    public void clickSubmit_withPreviousPage_callsSave_andNavigatesToPreviousPage() {
        getTester().startPage(
            new NewsletterTopicSortPage(Model.of(newsletter), new LoginPage(new PageParameters()).getPageReference()));
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:submit", "click");

        getTester().assertRenderedPage(LoginPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service).saveSortedNewsletterTopics(newsletter.getId(), topics);
    }

    @Test
    public void clickSubmit_withSaveThrowing_addsErrorMessage_andStaysOnPage() {
        doThrow(new RuntimeException("boom"))
            .when(service)
            .saveSortedNewsletterTopics(newsletter.getId(), topics);

        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:submit", "click");

        getTester().assertNoInfoMessage();
        getTester().assertErrorMessages("Unexpected error: boom");

        getTester().assertRenderedPage(NewsletterTopicSortPage.class);

        verify(service).saveSortedNewsletterTopics(newsletter.getId(), topics);
    }

    @Test
    public void clickCancel_withNoPreviousPage_doesNotCallSave_butNavigatesToPaperListPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:cancel", "click");

        getTester().assertRenderedPage(PaperListPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service, never()).saveSortedNewsletterTopics(anyInt(), anyList());
    }

    @Test
    public void clickCancel_withPreviousPage_doesNotCallSave_butNavigatesToPreviousPage() {
        getTester().startPage(
            new NewsletterTopicSortPage(Model.of(newsletter), new LoginPage(new PageParameters()).getPageReference()));
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:cancel", "click");

        getTester().assertRenderedPage(LoginPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service, never()).saveSortedNewsletterTopics(anyInt(), anyList());
    }

}