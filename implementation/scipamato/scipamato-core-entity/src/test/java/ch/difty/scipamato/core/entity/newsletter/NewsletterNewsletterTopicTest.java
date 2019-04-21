package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.Test;

public class NewsletterNewsletterTopicTest {

    private final int    newsletterId      = 1;
    private final int    newsletterTopicId = 2;
    private final int    sort              = 3;
    private final String title             = "title";

    private final NewsletterNewsletterTopic nnt = new NewsletterNewsletterTopic(newsletterId, newsletterTopicId, sort,
        title);

    @Test
    public void getters() {
        assertThat(nnt.getNewsletterId()).isEqualTo(newsletterId);
        assertThat(nnt.getNewsletterTopicId()).isEqualTo(newsletterTopicId);
        assertThat(nnt.getSort()).isEqualTo(sort);
        assertThat(nnt.getTitle()).isEqualTo("title");
        assertThat(nnt.getDisplayValue()).isEqualTo(nnt.getTitle());
    }

    @Test
    public void validatingToString() {
        assertThat(nnt.toString()).isEqualTo(
            "NewsletterNewsletterTopic(newsletterId=1, newsletterTopicId=2, sort=3, title=title)");
    }
}