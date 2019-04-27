package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;

class NewsletterFilterTest {

    private final NewsletterFilter f = new NewsletterFilter();

    @Test
    void getAndSet() {
        f.setIssueMask("issueMask");
        f.setPublicationStatus(PublicationStatus.CANCELLED);
        f.setNewsletterTopic(new NewsletterTopic(1, "foo"));

        assertThat(f.getIssueMask()).isEqualTo("issueMask");
        assertThat(f.getPublicationStatus()).isEqualTo(PublicationStatus.CANCELLED);
        assertThat(f
            .getNewsletterTopic()
            .getId()).isEqualTo(1);

        assertThat(f.toString()).isEqualTo(
            "NewsletterFilter(issueMask=issueMask, publicationStatus=CANCELLED, newsletterTopic=NewsletterTopic(title=foo))");
    }

    @Test
    void equals() {
        EqualsVerifier
            .forClass(NewsletterFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void assertEnumFields() {
        assertThat(NewsletterFilter.NewsletterFilterFields.values())
            .extracting("name")
            .containsExactly("issueMask", "publicationStatus", "newsletterTopic");
    }

}