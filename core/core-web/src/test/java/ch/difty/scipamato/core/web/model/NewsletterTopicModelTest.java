package ch.difty.scipamato.core.web.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;

class NewsletterTopicModelTest extends ModelTest {

    @Test
    void loading_delegatesToNewsletterTopicService() {
        String languageCode = "de";

        final List<NewsletterTopic> topics = new ArrayList<>();
        topics.add(new NewsletterTopic(1, "t1"));
        topics.add(new NewsletterTopic(2, "t2"));

        when(newsletterTopicServiceMock.findAll(languageCode)).thenReturn(topics);

        final NewsletterTopicModel model = new NewsletterTopicModel("de");

        assertThat(model.load())
            .extracting(NewsletterTopic.NewsletterTopicFields.TITLE.getFieldName())
            .containsExactly("t1", "t2");

        verify(newsletterTopicServiceMock).findAll(languageCode);

        verifyNoMoreInteractions(newsletterTopicServiceMock);
    }
}
