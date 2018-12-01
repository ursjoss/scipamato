package ch.difty.scipamato.core.web.newsletter.list;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
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
            getTester().assertLabel(bb + String.valueOf(i) + ":item", "topic" + (i + 1));

        bb = b + ":submit";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertLabel(bb + ":label", "Save");

        bb = b + ":cancel";
        getTester().assertComponent(bb, BootstrapAjaxButton.class);
        getTester().assertLabel(bb + ":label", "Cancel");
    }

    @Test
    public void clickSubmit_callsSave_andNavigatesAwayFromPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:submit", "click");

        getTester().assertRenderedPage(PaperListPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service).saveSortedNewsletterTopics(newsletter.getId(), topics);
    }

    @Test
    public void clickCancel_doesNotCallSave_butNavigatesAwayFromPage() {
        getTester().startPage(makePage());
        getTester().assertRenderedPage(getPageClass());

        getTester().executeAjaxEvent("form:cancel", "click");

        getTester().assertRenderedPage(PaperListPage.class);

        getTester().assertNoErrorMessage();
        getTester().assertNoInfoMessage();

        verify(service, never()).saveSortedNewsletterTopics(anyInt(), anyList());
    }
}