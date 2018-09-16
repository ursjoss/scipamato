package ch.difty.scipamato.core.entity.newsletter;

import static org.assertj.core.api.Assertions.assertThat;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class NewsletterFilterTest {

    private final NewsletterFilter f = new NewsletterFilter();

    @Test
    public void getAndSet() {
        f.setIssueMask("issueMask");
        f.setPublicationStatus(PublicationStatus.CANCELLED);

        assertThat(f.getIssueMask()).isEqualTo("issueMask");
        assertThat(f.getPublicationStatus()).isEqualTo(PublicationStatus.CANCELLED);

        assertThat(f.toString()).isEqualTo("NewsletterFilter(issueMask=issueMask, publicationStatus=CANCELLED)");
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(NewsletterFilter.class)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void assertEnumFields() {
        assertThat(NewsletterFilter.NewsletterFilterFields.values())
            .extracting("name")
            .containsExactly("issueMask", "publicationStatus");
    }

}